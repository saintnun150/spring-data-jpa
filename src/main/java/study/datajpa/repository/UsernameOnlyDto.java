package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;
    
    // 이 생성자에 넘긴 파라미터 이름으로 dto 가능
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
