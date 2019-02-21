package Utils;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.io.File;
import java.util.EnumSet;

/**
 * Генерирует схему БД (SQL-команды)
 * на основе классов-сущностей Hibernate
 */
public class SchemaGenerator {
    public static final String SCRIPT_FILE = "./db/exportScript.sql";

    private static SchemaExport getSchemaExport(){
        SchemaExport export = new SchemaExport();

        File outputFile = new File(SCRIPT_FILE);
        String outputPath = outputFile.getAbsolutePath();

        export.setDelimiter(";");
        export.setOutputFile(outputPath);
        //Non stop if error
        export.setHaltOnError(false);

        return export;
    }

    public static void dropDataBase(SchemaExport export, Metadata metadata){
        //TargetType.DATABASE - Execute on Database
        //TargetType.SCRIPT - Write Script file
        //TargetType.STDOUT - Write log to Console
        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.DATABASE, TargetType.SCRIPT, TargetType.STDOUT);

        export.drop(targetTypes, metadata);
    }

    public static void createDataBase(SchemaExport export, Metadata metadata){
        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.DATABASE, TargetType.SCRIPT, TargetType.STDOUT);

        SchemaExport.Action action = SchemaExport.Action.CREATE;

        export.execute(targetTypes, action, metadata);
        System.out.println("Ok");
    }

    public static void main(String[] args) {
        String configFileName = "hibernate.cfg.xml";
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(configFileName).build();
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();

        SchemaExport export = getSchemaExport();
        System.out.println("Drop DB...");
        dropDataBase(export, metadata);
        System.out.println("Create DB...");
        createDataBase(export, metadata);
        System.exit(1);
    }


}