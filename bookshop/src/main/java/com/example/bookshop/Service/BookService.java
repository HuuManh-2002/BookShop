package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Author;
import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.Category;
import com.example.bookshop.Entity.Publisher;
import com.example.bookshop.EntityDto.Reponse.BookReponse;
import com.example.bookshop.EntityDto.Request.BookRequest;
import com.example.bookshop.EntityDto.Update.BookUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.BookMapper;
import com.example.bookshop.Repository.AuthorRepository;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.CategoryRepository;
import com.example.bookshop.Repository.PublisherRepository;

@Service
public class BookService {

        @Autowired
        BookRepository bookRepository;
        @Autowired
        AuthorRepository authorRepository;
        @Autowired
        CategoryRepository categoryRepository;
        @Autowired
        PublisherRepository publisherRepository;

        @Autowired
        BookMapper bookMapper;

        public Page<BookReponse> getAll(Pageable pageable) {
                // Gọi phương thức findAll(Pageable) của repository để lấy một Page<Book>
                Page<Book> booksPage = bookRepository.findAll(pageable);

                // Sử dụng stream và map để chuyển đổi từng đối tượng Book trong Page thành
                // BookReponse
                // Phương thức map của Page tự động xử lý việc phân trang, tổng số phần tử, v.v.
                return booksPage.map(book -> bookMapper.toBookReponse(book));
        }

        public List<BookReponse> getByPublisher(Long publisher_id) {
                Publisher publisher = publisherRepository.findById(publisher_id)
                                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
                List<Book> books = bookRepository.findByPublisher(publisher);
                return books.stream()
                                .map(bookMapper::toBookReponse)
                                .collect(Collectors.toList());
        }

        public List<BookReponse> getByAuthor(Long author_id) {
                Author author = authorRepository.findById(author_id)
                                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
                List<Book> books = bookRepository.findByAuthor(author);
                List<BookReponse> bookReponses = new ArrayList<>();
                for (Book book : books) {
                        bookReponses.add(bookMapper.toBookReponse(book));
                }
                return bookReponses;
        }

        public List<BookReponse> getByCategory(Long category_id) {
                Category category = categoryRepository.findById(category_id)
                                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
                List<Book> books = bookRepository.findByCategory(category);
                List<BookReponse> bookReponses = new ArrayList<>();
                for (Book book : books) {
                        bookReponses.add(bookMapper.toBookReponse(book));
                }
                return bookReponses;
        }

        public BookReponse get(Long id) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
                return bookMapper.toBookReponse(book);
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public BookReponse create(BookRequest bookRequest) {
                Book book = bookMapper.toBook(bookRequest);
                Category category = categoryRepository.findById(bookRequest.getCategory_id())
                                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
                Publisher publisher = publisherRepository.findById(bookRequest.getPublisher_id())
                                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
                Author author = authorRepository.findById(bookRequest.getAuthor_id())
                                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
                book.setAuthor(author);
                book.setCategory(category);
                book.setPublisher(publisher);
                bookRepository.save(book);
                return bookMapper.toBookReponse(book);
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public BookReponse update(Long id, BookUpdate bookUpdate) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
                Category category = categoryRepository.findById(bookUpdate.getCategory_id())
                                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
                Publisher publisher = publisherRepository.findById(bookUpdate.getPublisher_id())
                                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
                Author author = authorRepository.findById(bookUpdate.getAuthor_id())
                                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
                book.setAuthor(author);
                book.setCategory(category);
                book.setPublisher(publisher);
                book.setName(bookUpdate.getName());
                book.setPrice(bookUpdate.getPrice());
                book.setPageNumber(bookUpdate.getPageNumber());
                book.setPublishYear(bookUpdate.getPublishYear());
                book.setDescription(bookUpdate.getDescription());

                bookRepository.save(book);
                return bookMapper.toBookReponse(book);
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public void delete(Long id) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
                bookRepository.delete(book);

        }
}
