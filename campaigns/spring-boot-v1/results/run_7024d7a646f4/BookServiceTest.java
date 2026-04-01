package com.alibou.security.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

 @Mock
 private BookRepository repository;

 @InjectMocks
 private BookService service;

 @Test
 public void save() {
 var request = BookRequest.builder()
 .id(1L)
 .author("Autor")
 .isbn("ISBN")
 .build();

 service.save(request);

 verify(repository, times(1)).save(any());
 }

 @Test
 public void findAll() {
 var libros = new ArrayList<Book>();
 when(repository.findAll()).thenReturn(libros);

 var resultado = service.findAll();

 assertEquals(libros, resultado);
 verify(repository, times(1)).findAll();
 }
}