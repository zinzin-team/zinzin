package com.fanclub.zinzin.domain.searcher.service;

import com.fanclub.zinzin.domain.searcher.dto.SearchedResultDto;
import com.fanclub.zinzin.domain.searcher.dto.SearcherResponse;
import com.fanclub.zinzin.domain.searcher.repository.SearcherRepository;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.code.SearcherErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearcherService {

    private final SearcherRepository searcherRepository;

    public SearcherResponse searchBySearchId(HttpServletRequest request, String searchId){
        if(searchId == null || searchId.replace(" ","").isEmpty()){
            throw new BaseException(SearcherErrorCode.INVALID_SEARCH_ID);
        }

        if(request.getAttribute("memberId") == null){
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Long memberId = (Long)request.getAttribute("memberId");
        SearchedResultDto searchedResultDto = searcherRepository.findPersonBySearchId(memberId, searchId);
        return SearcherResponse.of(searchedResultDto.getId()!=null, searchedResultDto);
    }
}
