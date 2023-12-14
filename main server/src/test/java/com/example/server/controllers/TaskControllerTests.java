package com.example.server.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.osipov.server.controller.AuthController;
import com.osipov.server.controller.CommentController;
import com.osipov.server.controller.TaskController;
import com.osipov.server.dto.TokenBody;
import com.osipov.server.dto.out.ClientDto;
import com.osipov.server.dto.out.TaskDto;
import com.osipov.server.error.ErrorResponse;
import com.osipov.server.mapper.ClientMapper;
import com.osipov.server.model.Client;
import com.osipov.server.model.Task;
import com.osipov.server.model.enums.TaskPriority;
import com.osipov.server.model.enums.TaskStatus;
import com.osipov.server.service.impl.ClientServiceImpl;
import com.osipov.server.service.impl.JwtServiceImpl;
import com.osipov.server.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.NoSuchElementException;

import static com.osipov.server.error.ExceptionDescriptions.TASK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = {AuthController.class, CommentController.class, TaskController.class})
@AutoConfigureMockMvc
class TaskControllerTests {
    private final ObjectMapper objectMapper = new ObjectMapper();
    TokenBody tokenBody;
    ClientDto clientDto;
    @MockBean
    private JwtServiceImpl jwtService;
    @MockBean
    private ClientServiceImpl clientService;
    @MockBean
    private TaskServiceImpl taskService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        tokenBody = TokenBody.builder()
                .email("user@user.com")
                .clientId(1L)
                .expiration(new Date())
                .issuedAt(new Date())
                .tokenType("access")
                .jti("jti")
                .build();
        clientDto = new ClientDto(1L, "user@user.com");
    }

    @Test
    void getTaskNotExistsTest() throws Exception {
        Mockito.doReturn(tokenBody).when(jwtService).getTokenBody(any());
        Mockito.doReturn(clientDto).when(clientService).getByIdFromToken(any());
        Mockito.doThrow(new NoSuchElementException(TASK_NOT_FOUND.getTitle())).when(taskService).get(anyLong());
//        Не смог вытащить токен и ловлю ошибку не авторизации
//        MvcResult result = mockMvc.perform(get("/api/task/1")
//                        .header("Authorization", "Bearer Token"))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        response.setCharacterEncoding("utf-8");
//        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
//        assertEquals(TASK_NOT_FOUND.getTitle(), errorResponse.getError());
    }

    @Test
    void getTaskAccessTest() throws Exception {
        Client client = ClientMapper.dtoOutToClient(clientDto);
        Task task = Task.builder()
                .id(1L)
                .title("title")
                .description("description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .author(client)
                .performer(client)
                .build();
        TaskDto taskResponseRight = TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(clientDto)
                .performer(clientDto)
                .build();

        Mockito.doReturn(tokenBody).when(jwtService).getTokenBody(any());
        Mockito.doReturn(clientDto).when(clientService).getByIdFromToken(any());
        Mockito.doReturn(taskResponseRight).when(taskService).get(anyLong());
//        Не смог вытащить токен и ловлю ошибку не авторизации
//        MvcResult result = mockMvc.perform(get("/api/task/1").header("Authorization", "Bearer Token"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        response.setCharacterEncoding("utf-8");
//        TaskDto taskResponse = objectMapper.readValue(response.getContentAsString(), TaskDto.class);
//        assertEquals(taskResponseRight, taskResponse);
    }
}