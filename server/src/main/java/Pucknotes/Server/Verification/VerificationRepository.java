package Pucknotes.Server.Verification;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationRepository extends MongoRepository<Verification, String> {
    public Optional<Verification> findByToken(String token);
}