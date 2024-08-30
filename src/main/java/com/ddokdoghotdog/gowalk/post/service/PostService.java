package com.ddokdoghotdog.gowalk.post.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Board;
import com.ddokdoghotdog.gowalk.entity.MediaUrlList;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostWriteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetDetailResponseDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetListResponseDTO;
import com.ddokdoghotdog.gowalk.post.repository.BoardRepository;
import com.ddokdoghotdog.gowalk.post.repository.MediaUrlRepository;
import com.ddokdoghotdog.gowalk.post.repository.PostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final MediaUrlRepository mediaUrlRepository;
	private final BoardRepository boardRepository;

	@Transactional(readOnly = true)
	public List<PostGetListResponseDTO> getPostList(Long boardid) {
		List<Post> posts = postRepository.findByBoardId(boardid)
	            .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
		
		return posts.stream()
	            .map(post -> PostGetListResponseDTO.builder()
	                    .postid(post.getId())
	                    .boardid(post.getBoard().getId())
	                    .title(post.getTitle())
	                    .nickname(post.getMember().getNickname())
	                    .createdAt(post.getCreatedAt())
	                    .updatedAt(post.getUpdatedAt())
	                    .likesCount(0L) // 테스트용
	                    .commentsCount(0L) // 테스트용
	                    .img(post.getMediaUrls().isEmpty() ? null : post.getMediaUrls().get(0).getMediaUrl()) // 첫 번째 이미지를 대표 이미지로 사용
	                    .build())
	            .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public PostGetDetailResponseDTO getPostDetails(Long postid) {
	    // 게시글 조회
	    Post post = postRepository.findById(postid)
	            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

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
	                    comment.getCreatedAt(),
	                    comment.getUpdatedAt()))
	            .collect(Collectors.toList()) : Collections.emptyList();

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
	            .likesCount(0)  // 일단 테스트로 0
	            .commentsCount(commentDTOs.size())
	            .imgList(imageUrl)
	            .comments(commentDTOs)
	            .build();
	}

	@Transactional
	public Post createPost(PostWriteRequestDTO dto) {
		
		Member member = memberRepository.findById(dto.getMemberid())
		        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

	    Board board = boardRepository.findById(dto.getBoardid())
	        .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
		
	    Post post = Post.builder()
	            .title(dto.getTitle())
	            .contents(dto.getContents())
	            .member(member)
	            .board(board)
	            .build();

	    postRepository.save(post);

	    List<MediaUrlList> mediaUrlLists = dto.getMediaUrlList().stream()
	        .map(url -> new MediaUrlList(null, post, url))
	        .collect(Collectors.toList());

	    mediaUrlRepository.saveAll(mediaUrlLists);

	    post.setMediaUrls(mediaUrlLists); 

		return post;
	}

	// 게시글 수정
	@Transactional
	public Post editPost(Long postid, PostDetailUpdateRequestDTO dto) {
	
		Post post = postRepository.findById(postid)
				.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
		
		post.setTitle(dto.getTitle());
		post.setContents(dto.getContents());
//		post.setMediaUrls(post, dto.getImg());
		
		postRepository.save(post);
		
		return post;
	}
	
	// 게시글 삭제
	public void deletePost(Long postid, PostDetailDeleteRequestDTO dto) {
		
		Post post = postRepository.findById(postid)
				.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
		
		postRepository.delete(post);

	}
}
