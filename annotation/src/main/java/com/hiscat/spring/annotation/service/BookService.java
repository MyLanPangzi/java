package com.hiscat.spring.annotation.service;

import com.hiscat.spring.annotation.dao.BookDao;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @author hiscat
 */
@Service
@Data
public class BookService {

    /**
     * //@Qualifier("bookDao2")
     * //    @Autowired
     *
     * //@Resource(name = "bookDao2")
     */
    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
