package com.example.auth_system.repository;

import com.example.auth_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // ဒီမှာ ဘာမှ ထပ်ရေးစရာမလိုပါဘူး
    // JpaRepository ကနေ save, findAll, findById စတဲ့ method တွေကို အလိုအလျောက် ရသွားမှာပါ

    User findByUsername(String username);
}
