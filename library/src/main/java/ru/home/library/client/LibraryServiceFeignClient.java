package ru.home.library.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.home.library.core.dto.BookRequestDto;
import ru.home.library.core.dto.SuccessResponseDto;

import java.util.List;

@FeignClient("libraryServiceFeignClient")
public interface LibraryServiceFeignClient {

    @RequestMapping(method = RequestMethod.POST)
    SuccessResponseDto sendNewBookInfo(@RequestBody BookRequestDto newBookDto);

    @RequestMapping(method = RequestMethod.PUT)
    SuccessResponseDto takeBook(@RequestBody BookRequestDto newBookDto);

    @RequestMapping(method = RequestMethod.PUT, path = "/free")
    SuccessResponseDto returnBook(@RequestBody BookRequestDto newBookDto);

    @RequestMapping(method = RequestMethod.GET, path = "/free")
    List<Long> getFreeBooksList();
}
