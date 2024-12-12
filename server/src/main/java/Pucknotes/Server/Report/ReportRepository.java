package Pucknotes.Server.Report;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The ReportRepository interface provides the methods to perform CRUD (Create, Read, Update, Delete)
 * operations on the Report entity within the MongoDB database. It extends the MongoRepository interface,
 * which is part of Spring Data MongoDB, allowing for easy integration with the MongoDB data access layer.
 */
public interface ReportRepository extends MongoRepository<Report, String> {}
