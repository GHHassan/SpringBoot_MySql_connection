//package com.example.sqlconnect.model.Repositories;
//
//import com.example.sqlconnect.io.entity.UserEntity;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface UserRepository extends CrudRepository<UserEntity, Long> {
//    UserEntity findUserByEmail(String email);
//
//    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true) // Use native query with the actual table name
//    UserEntity findByUserId(@Param("userId") String userId);
//}
package com.example.sqlconnect.model.Repositories;

import com.example.sqlconnect.io.entity.UserRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserRecord, Long> {
    UserRecord findByUserId(String user_id);  // Ensure this method
    UserRecord findByEmail(String email);
}

