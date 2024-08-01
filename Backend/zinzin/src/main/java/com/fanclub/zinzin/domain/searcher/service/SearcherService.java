package com.fanclub.zinzin.domain.searcher.service;

import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.searcher.dto.SearchedMemberDto;
import com.fanclub.zinzin.domain.searcher.dto.SearcherResponse;
import com.fanclub.zinzin.global.error.code.SearcherErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearcherService {

    private final MemberInfoRepository memberInfoRepository;

    public SearcherResponse searchBySearchId(String searchId){
        if(searchId == null || searchId.replace(" ","").isEmpty()){
            throw new BaseException(SearcherErrorCode.INVALID_SEARCH_ID);
        }

        SearchedMemberDto searchedMemberDto = memberInfoRepository.findSearcherResponseBySearchId(searchId)
                                                .orElse(null);
        return SearcherResponse.of(searchedMemberDto!=null, searchedMemberDto);
    }
}
