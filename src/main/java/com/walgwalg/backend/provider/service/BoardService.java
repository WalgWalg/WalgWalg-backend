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
    public ResponseBoard.getBoard getBoard(Long boardId){
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
                .step_count(walk.getStep_count())
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
    public void deleteBoard(String userId, Long boardId){
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

    //좋아요
    @Transactional
    @Override
    public void addLike(String userid, Long boardId){
        User user = userRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());

        Likes like = likesRepository.findByUserAndBoard(user, board);
        if(like != null){ //이미 좋아요 눌렀을 경우
            throw new DuplicatedLikeException();
        }
        //좋아요 추가
        like = Likes.builder()
                .user(user)
                .board(board)
                .build();
        like = likesRepository.save(like);
        user.addLikes(like);
        board.addLikes(like);
    }
    @Transactional
    @Override
    public void deleteLikeBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());
        boardRepository.delete(board);
    }


    @Transactional
    @Override
    public void addScrap(String userid, RequestBoard.scrap requestDto){
        User user = userRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        User writer = userRepository.findByUserid(requestDto.getWriterId()); // 좋아요 누른 게시판의 작성자
        if(user == null){//작성자가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findByUserAndTimestamp(writer, requestDto.getWriteDate()); //작성자와 작성일자를 기준으로 게시판 찾기
        if(board ==null){
            throw new NotFoundBoardException();
        }
        Scrap scrap = scrapRepository.findByUserAndBoard(user, board);
        if(scrap != null){ //이미 스크랩 했을 경우
            throw new DuplicatedScrapException();
        }
        //스크랩 추가
       scrap = Scrap.builder()
                .user(user)
                .board(board)
                .build();
        scrapRepository.save(scrap);
        user.addScrap(scrap);
        board.addScrap(scrap);
    }
    @Override
    @Transactional
    public List<ResponseBoard.MyLike> listLikeBoard(String userid){
        User user = userRepository.findByUserid(userid); //유저 꺼내기
        if(user == null){//유저가 없으면
            throw new NotFoundUserException();
        }
        List<Likes> likeList = likesRepository.findByUser(user);//좋아요 리스트 꺼내기

        List<ResponseBoard.MyLike> boards =new ArrayList<>();

        for(Likes like : likeList){
            Board board = like.getBoard();
            boards.add(ResponseBoard.MyLike.of(board));
        }
        //Dto로 변환
        return boards;
    }
}
