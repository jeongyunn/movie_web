package com.example.movieweb.service;

import com.example.movieweb.model.User;
import com.example.movieweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service // 이 클래스가 비즈니스 로직을 처리하는 서비스 컴포넌트임을 나타냅니다. Spring은 이 클래스의 인스턴스를 생성하고 관리합니다.
public class UserService {

    private final UserRepository userRepository;

    @Autowired  // Spring의 의존성 주입 기능을 사용하여 UserRepository 빈을 UserService 클래스에 자동으로 주입합니다. 이를 통해 UserService 내에서 UserRepository의 메서드를 사용할 수 있습니다.
    public UserService(UserRepository userRepository) {  // UserService(UserRepository userRepository): 생성자를 통해 UserRepository를 주입받습니다.
        this.userRepository = userRepository;
    }

    public User saveOrUpdate(String socialId, String provider, String name, String email) {
        Optional<User> existingUser = userRepository.findBySocialIdAndProvider(socialId, provider); // 주어진 socialId와 provider로 이미 등록된 사용자가 있는지 userRepository.findBySocialIdAndProvider() 메서드를 사용하여 확인합니다.

        if (existingUser.isPresent()) {  // 이미 등록된 사용자가 있다면 기존 사용자 정보를 반환합니다. 
            User user = existingUser.get();
            // 필요한 경우 기존 사용자 정보 업데이트 (예: 이름 변경 등)
            return userRepository.save(user);
        } else {  // 새로운 사용자라면 User 객체를 생성하여 userRepository.save() 메서드를 사용하여 데이터베이스에 저장하고 저장된 User 객체를 반환합니다.
            User newUser = new User(socialId, provider, name, email);  
            return userRepository.save(newUser);
        }
    }

    public Optional<User> findBySocialIdAndProvider(String socialId, String provider) {  // findBySocialIdAndProvider(String socialId, String provider): 주어진 socialId와 provider로 사용자를 찾는 메서드를 UserRepository의 메서드를 호출하여 구현합니다.
        return userRepository.findBySocialIdAndProvider(socialId, provider);
    }
}