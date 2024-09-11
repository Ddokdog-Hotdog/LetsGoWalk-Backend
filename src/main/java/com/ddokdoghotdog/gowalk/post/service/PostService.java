package com.ddokdoghotdog.gowalk.post.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Board;
import com.ddokdoghotdog.gowalk.entity.MediaUrlList;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.entity.PostLike;
import com.ddokdoghotdog.gowalk.global.config.s3.S3Service;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostWriteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetDetailResponseDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetListResponseDTO;
import com.ddokdoghotdog.gowalk.post.repository.BoardRepository;
import com.ddokdoghotdog.gowalk.post.repository.MediaUrlRepository;
import com.ddokdoghotdog.gowalk.post.repository.PostLikeRepository;
import com.ddokdoghotdog.gowalk.post.repository.PostRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class PostService {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final MediaUrlRepository mediaUrlRepository;
	private final BoardRepository boardRepository;
	private final S3Service s3Service;
	private final PostLikeRepository postLikeRepository;

	//게시글 리스트 조회
	@Transactional(readOnly = true)
	public List<PostGetListResponseDTO> getPostList(Long boardid, Pageable pageable) {
	    Page<Post> posts = postRepository.findByBoardId(boardid, pageable);
	    
	    return posts.stream()
	            .map(post -> {
	                // 댓글 수 계산
	                long commentsCount = post.getComments() != null ? post.getComments().size() : 0L;

	                // 좋아요 수 계산
	                long likesCount = postLikeRepository.countByPostId(post.getId());

	                return PostGetListResponseDTO.builder()
	                        .postid(post.getId())
	                        .boardid(post.getBoard().getId())
	                        .title(post.getTitle())
	                        .nickname(post.getMember().getNickname())
	                        .createdAt(post.getCreatedAt())
	                        .updatedAt(post.getUpdatedAt())
	                        .likesCount(likesCount)
	                        .commentsCount(commentsCount)
	                        .img(post.getMediaUrls().isEmpty() ? null : post.getMediaUrls().get(0).getMediaUrl()) // 첫 번째 이미지를 대표 이미지로 사용
	                        .build();
	            })
	            .collect(Collectors.toList());
	}
	
	//게시글 종류 조회
	public List<Board> getAllBoards(){
		return boardRepository.findAll();
	}
	
	//내가 작성한 게시글 리스트 조회 
	@Transactional(readOnly = true)
	public List<PostGetListResponseDTO> getMyPostList(Long boardid, Long memberid, Pageable pageable) {
		Page<Post> posts = postRepository.findByBoardIdAndMemberId(boardid, memberid, pageable);
		
		 return posts.stream()
		            .map(post -> {
		                // 댓글 수 계산
		                long commentsCount = post.getComments() != null ? post.getComments().size() : 0L;

		                // 좋아요 수 계산
		                long likesCount = postLikeRepository.countByPostId(post.getId());

		                return PostGetListResponseDTO.builder()
		                        .postid(post.getId())
		                        .boardid(post.getBoard().getId())
		                        .title(post.getTitle())
		                        .nickname(post.getMember().getNickname())
		                        .createdAt(post.getCreatedAt())
		                        .updatedAt(post.getUpdatedAt())
		                        .likesCount(likesCount)
		                        .commentsCount(commentsCount)
		                        .img(post.getMediaUrls().isEmpty() ? null : post.getMediaUrls().get(0).getMediaUrl()) // 첫 번째 이미지를 대표 이미지로 사용
		                        .build();
		            })
		            .collect(Collectors.toList());
		
		}
	
	@Transactional(readOnly = true)
	public PostGetDetailResponseDTO getPostDetails(Long postid, Long memberId) {
	    // 게시글 조회
	    Post post = postRepository.findById(postid)
	            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
	    
	    boolean isLiked = postLikeRepository.existsByPostIdAndMemberId(postid, memberId);

	    // 게시글의 미디어 URL 리스트
	    List<String> imageUrl = post.getMediaUrls() != null ? post.getMediaUrls().stream()
	            .map(MediaUrlList::getMediaUrl)
	            .collect(Collectors.toList()) : Collections.emptyList();

	    // 게시글의 댓글
	    List<PostGetDetailResponseDTO.CommentDTO> commentDTOs = post.getComments() != null ? post.getComments().stream()
	            .map(comment -> new PostGetDetailResponseDTO.CommentDTO(
	                    comment.getId(),
	                    comment.getParentComment() != null ? comment.getParentComment().getId() : null,
	                    comment.getMember().getNickname(),
	                    comment.getContents(),
	                    comment.getMember().getProfileImageUrl(),
	                    comment.getCreatedAt(),
	                    comment.getUpdatedAt()))
	            .collect(Collectors.toList()) : Collections.emptyList();
	    // 게시글의 좋아요
	    long likesCount = postLikeRepository.countByPostId(post.getId());

	    // DTO 빌드
	    return PostGetDetailResponseDTO.builder()
	            .postid(post.getId())
	            .boardid(post.getBoard().getId())
	            .title(post.getTitle())
	            .contents(post.getContents())
	            .profileImageUrl(post.getMember().getProfileImageUrl())
	            .nickname(post.getMember().getNickname())
	            .memberid(post.getMember().getId())
	            .createdAt(post.getCreatedAt())
	            .updatedAt(post.getUpdatedAt())
	            .likesCount(likesCount)  
	            .commentsCount(commentDTOs.size())
	            .imgList(imageUrl)
	            .comments(commentDTOs)
	            .build();
	}
	
	// 게시글 등록
	@Transactional
    public Post createPost(PostWriteRequestDTO dto, List<MultipartFile> images, Principal principal) {
		
		Long memberId = Long.parseLong(principal.getName());
	    Board board = boardRepository.findById(dto.getBoardid())
	        .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

	    Post post = Post.builder()
	            .title(dto.getTitle())
	            .contents(dto.getContents())
	            .member(Member.builder().id(memberId).build())
	            .board(board)
	            .build();

	    postRepository.save(post);
 
	    List<MediaUrlList> mediaUrlLists = images.stream()
	            .map(image -> {
	                String imageUrl = s3Service.uploadFile(image, "posts");
	                return new MediaUrlList(null, post, imageUrl);
	            })
	            .collect(Collectors.toList());

	    mediaUrlRepository.saveAll(mediaUrlLists);
	    post.setMediaUrls(mediaUrlLists); 

		return post;
	}

	// 게시글 수정
	@Transactional
	public Post editPost(Long postid, PostDetailUpdateRequestDTO dto, List<MultipartFile> images, Long memberId) {
	    Post post = postRepository.findById(postid)
	            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
	    
	    if (!post.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
	    
	    post.setTitle(dto.getTitle());
	    post.setContents(dto.getContents());

	    // 기존 mediaUrls 컬렉션을 새 컬렉션으로 대체하지 않고, 기존의 컬렉션을 사용하여 업데이트
	    mediaUrlRepository.deleteByPostId(post.getId());
	    post.getMediaUrls().clear(); // 기존 컬렉션을 클리어

	    for (MultipartFile file : images) {
	        String imageUrl = s3Service.uploadFile(file, "posts");
	        MediaUrlList mediaUrl = new MediaUrlList(null, post, imageUrl);
	        post.getMediaUrls().add(mediaUrl); // 기존 컬렉션에 추가
	    }
	    mediaUrlRepository.saveAll(post.getMediaUrls());

	    return post;
	}
	
	// 게시글 삭제
	public void deletePost(Long postid, PostDetailDeleteRequestDTO dto, Long memberId) {
		
		Post post = postRepository.findById(postid)
				.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
		
		if (!post.getMember().getId().equals(memberId)) {
	        throw new BusinessException(ErrorCode.NO_PERMISSION);
	    }
		
		postRepository.delete(post);
	}
	
	//게시글 좋아요(토글)
	@Transactional
    public void toggleLike(Long postid, Long memberId) {

		Post post = postRepository.findById(postid)
				.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
		Optional<PostLike> optionalExistingLike = postLikeRepository.findByPostIdAndMemberId(postid, memberId);

		if (optionalExistingLike.isPresent()) {
		    postLikeRepository.deleteByPostIdAndMemberId(postid, memberId);
		} else {
		    PostLike like = PostLike.builder()
		            .post(post)
		            .member(post.getMember())
		            .build();
		    postLikeRepository.save(like);
		}
    }
	
	public Long getLikesCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
}
