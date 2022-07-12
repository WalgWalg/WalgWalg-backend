package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.ScrapServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.exception.errors.*;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.ScrapRepository;
import com.walgwalg.backend.repository.UsersRepository;
import com.walgwalg.backend.web.dto.ResponseScrap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService implements ScrapServiceInterface {
    private final UsersRepository usersRepository;
    private final BoardRepository boardRepository;
    private final ScrapRepository scrapRepository;


    @Transactional
    @Override
    public void addScrap(String userid, String boardId){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());

        Scrap scrap = scrapRepository.findByUsersAndBoard(user, board);
        if(scrap != null){ //이미 스크랩 했을 경우
            throw new DuplicatedScrapException();
        }
        //스크랩 추가
        scrap = Scrap.builder()
                .users(user)
                .board(board)
                .build();
        scrapRepository.save(scrap);
        user.addScrap(scrap);
        board.addScrap(scrap);
    }

    @Transactional
    @Override
    public List<ResponseScrap.MyScrap> getScrap(String userid){
        Users user = usersRepository.findByUserid(userid); //유저 꺼내기
        if(user == null){//유저가 없으면
            throw new NotFoundUserException();
        }
        List<Scrap> scrapList = scrapRepository.findByUsers(user);

        List<ResponseScrap.MyScrap> list = new ArrayList<>();

        for(Scrap scrap : scrapList){
            Board board = scrap.getBoard();
            list.add(ResponseScrap.MyScrap.of(board));
        }
        return list;
    }

    @Transactional
    @Override
    public void deleteScrap(String userid ,String boardId){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());
        Scrap scrap = scrapRepository.findByUsersAndBoard(user, board);
        if(scrap == null){
            throw new NotFoundScrapException();
        }
        scrapRepository.delete(scrap);
    }
}
