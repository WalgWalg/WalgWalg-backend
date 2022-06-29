package com.walgwalg.backend.provider.service;

import com.amazonaws.Response;
import com.walgwalg.backend.core.service.BoardServiceInterface;
import com.walgwalg.backend.entity.*;
import com.walgwalg.backend.exception.errors.DuplicatedLikeException;
import com.walgwalg.backend.exception.errors.DuplicatedScrapException;
import com.walgwalg.backend.exception.errors.NotFoundBoardException;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.repository.*;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {
    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final WalkRepository walkRepository;
    private final HashTagRepository hashTagRepository;

    @Transactional
    @Override
    public void registerBoard(String userid, RequestBoard.register requestDto){
        User user = userRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUserAndId(user, requestDto.getWalkId());
        if(walk == null){ //해당 산책이 없을 경우
            throw new NotFoundUserException();
        }
        //게시판 등록
        Board board = Board.builder()
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .walk(walk)
                .user(user)
                .build();
        board = boardRepository.save(board);
        //해시태그가 있을 경우

        if(!requestDto.getHashTags().isEmpty()){
            for(String item: requestDto.getHashTags()){
                HashTag hashTag = HashTag.builder()
                        .tag(item)
                        .board(board)
                        .build();
                hashTag = hashTagRepository.save(hashTag);
                board.addHashTag(hashTag);
            }
        }
    }
    @Transactional
    @Override
    public ResponseBoard.getBoard getBoard(String boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());
        Walk walk = board.getWalk();
        List<String> hashTagList = new ArrayList<>();
        if(!board.getHashTags().isEmpty()){//해시태그가 있으면
            for(HashTag tag : board.getHashTags()){
                hashTagList.add(tag.getTag());
            }
        }
        ResponseBoard.getBoard response = ResponseBoard.getBoard.builder()
                .title(board.getTitle())
                .contents(board.getContents())
                .hashTags(hashTagList)
                .step_count(walk.getStepCount())
                .distance(walk.getDistance())
                .calorie(walk.getCalorie())
                .course(walk.getCourse())
                .location(walk.getLocation())
                .nickname(board.getUser().getNickname())
                .likes(board.getLikesList().size())
                .build();
        return response;
    }
    @Transactional
    @Override
    public List<ResponseBoard.list> getMyBoard(String userId){
        User user = userRepository.findByUserid(userId);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        List<ResponseBoard.list> list = new ArrayList<>();

        List<Board> boardList = boardRepository.findByUser(user);
        if(!boardList.isEmpty()){//게시판이 있을 경우
            for(Board board : boardList){
                list.add(ResponseBoard.list.of(board));
            }
        }

        return list;
    }

    @Transactional
    @Override
    public void deleteBoard(String userId, String boardId){
        User user = userRepository.findByUserid(userId);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findByIdAndUser(boardId, user);
        if(board == null){
            throw new NotFoundBoardException();
        }
        boardRepository.delete(board);
    }
    @Transactional
    @Override
    public List<ResponseBoard.top> getBoardTop(){
        List<ResponseBoard.top> list = new ArrayList<>();
        List<Board> boardList =boardRepository.findTop5ByOrderByLikesListDesc();
        for(Board board : boardList){
            Walk walk = board.getWalk();
            ResponseBoard.top response = ResponseBoard.top.builder()
                    .image(walk.getCourse())
                    .parkName(walk.getLocation())
                    .build();
            list.add(response);

        }
        return list;
    }
}
