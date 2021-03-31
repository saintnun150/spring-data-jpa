package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    //open_projection : 엔티티를 다 가져와서 처리 하는것
    //close_projection : 정확한 해당 값만 가져오는 것
    //@Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
