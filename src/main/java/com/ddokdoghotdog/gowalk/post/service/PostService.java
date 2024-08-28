package com.ddokdoghotdog.gowalk.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.entity.PostLike;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.post.dto.request.PostWriteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetDetailResponseDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetListResponseDTO;
import com.ddokdoghotdog.gowalk.post.repository.PostLikeRepository;
import com.ddokdoghotdog.gowalk.post.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {public List<PostGetListResponseDTO> getPosts() {
		// TODO Auto-generated method stub
		return null;
	}

public PostGetDetailResponseDTO getPostDetails(Long postId) {
	// TODO Auto-generated method stub
	return null;
}

public Post createPost(PostWriteRequestDTO dto) {
	// TODO Auto-generated method stub
	return null;
}

public void deletePost(Long postId) {
	// TODO Auto-generated method stub
	
}
	
	
}
