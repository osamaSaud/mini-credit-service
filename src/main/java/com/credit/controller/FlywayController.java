package com.credit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javax.sql.DataSource;

@RestController
@RequestMapping("/api/flyway")
public class FlywayController {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FlywayController(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/info")
    public Map<String, Object> getFlywayInfo() {
        Map<String, Object> response = new HashMap<>();
        
        // Get Flyway info without forcing migration
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        
        // Get migration info instead of migrating
        MigrationInfoService migrationInfoService = flyway.info();
        MigrationInfo[] migrations = migrationInfoService.all();
        
        List<Map<String, Object>> migrationList = new ArrayList<>();
        if (migrations != null) {
            for (MigrationInfo migration : migrations) {
                Map<String, Object> migrationInfo = new HashMap<>();
                migrationInfo.put("version", migration.getVersion() != null ? migration.getVersion().toString() : "null");
                migrationInfo.put("description", migration.getDescription());
                migrationInfo.put("state", migration.getState().toString());
                migrationInfo.put("installedOn", migration.getInstalledOn());
                migrationInfo.put("type", migration.getType().toString());
                migrationList.add(migrationInfo);
            }
        }
        response.put("migrations", migrationList);
        
        // Check for schema_version table (older Flyway) or flyway_schema_history (newer Flyway)
        boolean hasFlywayTable = false;
        
        try {
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = 'credit_service_dev2' AND " +
                "(table_name = 'schema_version' OR table_name = 'flyway_schema_history')"
            );
            
            hasFlywayTable = tables.size() > 0;
            response.put("flywayTablesFound", tables);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        
        response.put("hasFlywayTable", hasFlywayTable);
        response.put("databaseName", "credit_service_dev2");
        
        // List all tables
        try {
            List<Map<String, Object>> allTables = jdbcTemplate.queryForList(
                "SHOW TABLES"
            );
            response.put("allTables", allTables);
        } catch (Exception e) {
            response.put("allTablesError", e.getMessage());
        }
        
        return response;
    }
} 