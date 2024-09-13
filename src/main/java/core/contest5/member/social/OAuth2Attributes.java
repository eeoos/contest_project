package core.contest5.member.social;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {

    private final String name;
    private final String email;
    private final String profileImage;

    @Builder
    public OAuth2Attributes(String name, String email, String profileImage) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        }

        throw new RuntimeException("잘못된 OAuth2 provider입니다.");
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .profileImage((String) kakaoProfile.get("profile_image_url"))
                .build();
    }

}
