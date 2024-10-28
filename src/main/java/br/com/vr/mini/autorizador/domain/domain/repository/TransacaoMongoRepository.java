package br.com.vr.mini.autorizador.domain.domain.repository;

import br.com.vr.mini.autorizador.domain.domain.model.TransacaoMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoMongoRepository extends MongoRepository<TransacaoMongoDB, String> {

}
