package com.example.ecohero.service;

import com.example.ecohero.entity.User;
import com.example.ecohero.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final String defaultProfileImage = "/images/default.png"; // 기본 프로필 이미지 경로
    private final String uploadDir = "uploads/"; // 업로드 디렉토리

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 ID로 사용자 조회
    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null); // 사용자가 없으면 null 반환
    }

    // 아이디 중복 체크
    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    // 이메일 중복 체크
    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    // 회원가입 처리
    public void registerUser(String username, String password, String email, String name, String phone,
                             String address, String zipcode, String birthdate, MultipartFile profileImage) throws Exception {
        // 아이디 중복 확인
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        String profileImagePath = defaultProfileImage; // 기본 이미지로 초기화
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImagePath = saveProfileImage(profileImage); // 프로필 이미지 저장
        }
        // 새 사용자 생성
        User newUser = new User(username, encodedPassword, email, name, phone, address, zipcode, birthdate, profileImagePath);
        userRepository.save(newUser);
    }

    // 사용자 정보 수정 (프로필 이미지 업데이트 포함)
    @Transactional
    public void updateUserProfile(Long id, String email, String phone, String address, String birthdate) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

        // 로그 추가
        System.out.println("Before Update: " + user);

        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setBirthdate(birthdate);

        // 로그 추가
        System.out.println("After Update: " + user);

        userRepository.save(user);  // 수정된 정보를 DB에 저장
    }

    // 프로필 이미지 저장
    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String filePath = uploadDir + System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
        File file = new File(filePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 디렉토리 생성
        }

        profileImage.transferTo(file);
        return "/" + filePath; // 저장된 파일 경로 반환
    }

    // 로그인 처리
    public User authenticateUser(String username, String password) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(username); // 아이디로 사용자 조회
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 비밀번호 검증
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            } else {
                throw new Exception("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new Exception("존재하지 않는 아이디입니다.");
        }
    }
}
