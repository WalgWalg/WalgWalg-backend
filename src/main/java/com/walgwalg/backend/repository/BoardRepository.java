package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    Board findByUsersAndTitle(Users users, String title);
    List<Board> findByUsers(Users users);
    Board findByIdAndUsers(String id ,Users users);

    @Query("select b from Board b join b.likesList l group by b.id order by l.size desc")
    List<Board> findTop5ByOrderByLikesListDesc(Pageable pageable);

}
