# Understanding Flyway Schema Migrations

## What are Database Migrations?

Database migrations are a way to manage changes to database schemas over time. Instead of manually modifying database schemas directly, migrations provide a systematic approach to track, apply, and version database changes.

## What is Flyway?

Flyway is a popular database migration tool that helps manage database schema changes in a version-controlled, organized, and repeatable way. It supports numerous database engines including MySQL, PostgreSQL, Oracle, and more.

## Key Concepts in Flyway

1. **Migrations**: Scripts that apply changes to the database. These can be written in SQL or Java.
2. **Versioned Migrations**: The primary type of migration, named with a version number (e.g., V1__Create_tables.sql).
3. **Migration History Table**: Flyway creates a table (default name: flyway_schema_history) to track which migrations have been applied.
4. **Baseline**: A way to start using Flyway with an existing database.

## Benefits of Using Flyway

1. **Version Control**: Database schema changes are versioned similarly to code.
2. **Repeatability**: The same migrations can be applied to different environments consistently.
3. **Automated Deployment**: Migrations can be part of CI/CD pipelines.
4. **Team Collaboration**: Multiple team members can contribute changes without conflicts.
5. **Rollback Capability**: Organized way to handle schema rollbacks if needed.

## How Flyway Works in Our Project

### Configuration

In our `application.properties`, we've configured Flyway with:

```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

And changed JPA's `ddl-auto` setting from `update` to `validate`:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This means:
1. We're no longer letting Hibernate automatically update our schema
2. Flyway will manage all schema changes through migration scripts
3. Hibernate will only validate that the schema matches our entity models

### Migration Scripts

Our migration scripts are stored in `src/main/resources/db/migration` and follow the Flyway naming convention:

1. **V1__Create_initial_schema.sql**: Creates our initial tables based on our entity models
2. **V2__Add_customer_relation_to_salary_certificate.sql**: Adds a relationship between tables

### Naming Convention for Migration Files

Flyway migration files follow a specific naming pattern:
- **V** for versioned migrations (the most common type)
- **Version number**: Usually integers separated by underscores or dots (e.g., 1, 1_1, 1.1)
- **Double underscore**: Separates the version from the description
- **Description**: A brief description of what the migration does, with words separated by underscores
- **.sql**: File extension for SQL-based migrations

Example: `V1_2__Add_new_customer_columns.sql`

## Best Practices for Using Flyway

1. **Never modify existing migrations**: Once a migration has been applied and committed, treat it as immutable.
2. **Create new migrations for changes**: Always create a new migration file for new changes.
3. **Keep migrations small and focused**: Each migration should do one logical change.
4. **Test migrations thoroughly**: Test migrations in a development environment before applying to production.
5. **Include both "up" and "down" logic**: When possible, include how to rollback a change.
6. **Use descriptive names**: Migration file names should clearly indicate what the migration does.
7. **Document complex migrations**: Add comments in SQL files to explain complex changes.

## Example Workflow

1. Developer identifies need for database schema change
2. Developer creates a new migration script with an incremented version number
3. Developer tests the migration locally
4. Migration is committed to version control
5. CI/CD pipeline applies the migration to test environment
6. After testing, the migration is applied to production

## Common Flyway Commands

In a Spring Boot application, Flyway is integrated with the application lifecycle, but you can also run Flyway commands manually using the Maven plugin:

```bash
# Apply migrations
mvn flyway:migrate

# Get information about migrations
mvn flyway:info

# Clean the database (remove all objects)
mvn flyway:clean

# Validate applied migrations against available ones
mvn flyway:validate
```

## Troubleshooting Common Issues

1. **Migration checksum mismatch**: This happens when you change an existing migration file. Always create a new migration instead.
2. **Failed migration**: If a migration fails halfway, you may need to repair the flyway_schema_history table.
3. **Out-of-order migrations**: By default, Flyway expects migrations to be applied in order. You can enable out-of-order migrations with a configuration setting.
4. **Baseline on an existing database**: Use baseline to start using Flyway with an existing database. 