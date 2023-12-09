package com.example.hotelswebapp.services;

import com.example.hotelswebapp.entity.ERole;
import com.example.hotelswebapp.entity.HotelRoomEntity;
import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.exception.UsernameAlreadyExistsException;
import com.example.hotelswebapp.repos.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final HotelRoomService hotelRoomService;

    public Optional<UserEntity> findByLogin(String login){
        return userRepo.findByLogin(login);
    }

    private BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public void saveUser(UserEntity userEntity) throws UsernameAlreadyExistsException {
        if (isUsernameUnique(userEntity.getLogin())) {
            userEntity.setPassword(encoder().encode(userEntity.getPassword()));
            userRepo.save(userEntity);
        } else {
            throw new UsernameAlreadyExistsException("Такой логин уже существует");
        }
    }

    public boolean isUsernameUnique(String username) {
        return !userRepo.findByLogin(username).isPresent();
    }

    public UserEntity getAuthUser(String login){
        return userRepo.findByLogin(login).get();
    }

    public List<UserEntity> getAllUsers() {
        Sort sort = Sort.by("id");
        return userRepo.findAll(sort);
    }

    public void addUserInfo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", auth);
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        model.addAttribute("roles", roles);
    }

    public void updateRole(int userId, String newRole){
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setRole(ERole.valueOf(newRole));
            userRepo.save(user);
        }
    }
    @Transactional
    public void deleteUser(int id){
        UserEntity userToDelete = userRepo.findById(id).get();
        UserEntity adminUser = userRepo.findByLogin("admin").get();
        List<HotelRoomEntity> hotelRooms = userToDelete.getHotelRoomEntities();
        for (HotelRoomEntity room : hotelRooms) {
            room.setUserEntity(adminUser);
            hotelRoomService.saveHotelRoom(room);
        }
        userRepo.delete(userToDelete);
    }
}

/*
подскажи как на этой странице при скролле
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org" lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Hotel</title>
        <link rel="stylesheet" type="text/css" href="../static/css/styles.css" th:href="@{/css/styles.css}">
    </head>
    <header th:insert="~{blocks/header :: header}"></header>
    <div class="centered">
        <div th:if="${errorOfIncorrectDate}" class="alert-danger">
            <p th:text="${errorOfIncorrectDate}"></p>
        </div>
        <div th:if="${errorOfNotYourRoom}" class="alert-danger">
            <p th:text="${errorOfNotYourRoom}"></p>
        </div>
        <div th:if="${errorOfIncorrectInputPrice}" class="alert-danger">
            <p th:text="${errorOfIncorrectInputPrice}"></p>
        </div>
    </div>
    <body>
    <div class="review">
    <div class="container">
        <div class="row">
            <div class="filter">
                <form th:action="@{/search}" th:method="get">
                    <fieldset>
                        <legend>Бронирование номеров в отеле</legend>
                            <label for="roomName">Название номера:</label>
                            <input type="text" id="roomName" name="roomName" placeholder="Введите название номера">

                            <label for="checkInDate">Дата заезда:</label>
                            <input type="date" id="checkInDate" name="checkInDate" min="2023-10-18">

                            <label for="checkOutDate">Дата выезда:</label>
                            <input type="date" id="checkOutDate" name="checkOutDate" min="2023-10-18">

                            <label for="amountOfSleepers">Количество гостей:</label>
                            <select id="amountOfSleepers" name="amountOfSleepers">
                                <option value="1">1 гость</option>
                                <option value="2">2 гостя</option>
                                <option value="3">3 гостя</option>
                                <option value="4">4 гостя</option>
                            </select>
                            <div>
                                <label>Диапазон цен:</label>
                                <div class="row">
                                    <div class="col-left">
                                        <input type="number" id="minPrice" name="minPrice" placeholder="Минимальная цена (в $)">
                                    </div>
                                    <div class="col-right">
                                        <input type="number" id="maxPrice" name="maxPrice" placeholder="Максимальная цена (в $ )">
                                    </div>
                                </div>
                            </div>
                    </fieldset>
                        <button type="submit" class="sign-btn">Поиск</button>
                </form>
            </div>
        </div>
    </div>
        <div class="links">
        <h1 th:if="${not rooms.isEmpty()}" class="list-of-rooms" >Список номеров</h1>
        <ul class="room-list">
            <li th:each="room, indx : ${rooms.content}">
                <a th:href="@{'/post?roomId=' + ${room.id}}" class="room-link">
                <div class="room">
                    <img th:src="@{'/photos/' + ${room.photos.get(0)}}" alt="Изображение номера" width="150" height="150">
                    <div class="room-details-main">
                        <h2>
                            Название номера:
                            <span th:text="${room.name + ' ' + averageRatings[indx.index]}"></span>
                            <span>&#9733;</span>
                        </h2>
                        <p>Спальных мест: <span th:text="${room.amountOfSleepers}"></span></p>
                    <p>Услуги:
                    <ul>
                        <li class="services" th:each="service : ${room.services}">
                            <span th:text="${service}"></span>
                        </li>
                    </ul>
                    </p>
                        <p>Цена за день: <span th:text="${room.pricePerDay} + '$'"></span></p>
                    </div>
                </div>
                </a>
            </li>
        </ul>

    <div th:if="${rooms.totalPages > 1}">
        <ul class="pagination">
            <li th:class="${rooms.number == 0} ? 'disabled'">
                <a th:if="${rooms.number != 0}" th:href="@{'/?page=' + ${rooms.number - 1}}">
                    <span class="arrow-circle">&laquo;</span>
                </a>
            </li>

            <li th:class="${rooms.number == 0} ? 'disabled'">
                <a th:if="${rooms.number != rooms.totalPages - 1}" th:href="@{'/?page=' + ${rooms.number + 1}}">
                    <span class="arrow-circle">&raquo;</span>
                </a>
            </li>
        </ul>
    </div>
    </div>
    </div>

    <footer th:insert="~{blocks/footer :: footer}"></footer>
    <script>
        const currentDate = new Date();

        const formattedCurrentDate = currentDate.toISOString().split('T')[0];

        document.getElementById('checkInDate').min = formattedCurrentDate;
        document.getElementById('checkOutDate').min = formattedCurrentDate;
    </script>
    </body>
    </html>
container перемещался и всегда был в центре экрана
 */
