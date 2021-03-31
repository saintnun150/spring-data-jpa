package study.datajpa.repository;

public interface NestedClosedProjections {
    
    //첫번째 있는 member(ROOT 엔티티)의 username은 최적화가 되어서 해당 데이터만 select해오지만
    //두번째 팀은 최적화가 안돼서 select절에서 다 가져온 뒤 원하는 데이터를 출력함
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }

}
