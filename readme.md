# Jooq codegen for Sqlite with Flyway and Gradle

Sample configuration that on `gradle build`:
- creates temporary Sqlite database
- runs all flyway migrations from `src/main/sql` against the temporary db
- runs Jooq plugin to generate code for schema from the temporary db
- adds the generated code into the source sets, so it's available to use in java 