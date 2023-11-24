package com.example.hotelswebapp.services;

import com.example.hotelswebapp.entity.HotelRoomEntity;
import com.example.hotelswebapp.entity.ReviewOfRoom;
import com.example.hotelswebapp.repos.ReviewOfRoomRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewOfRoomService {
    private final ReviewOfRoomRepo reviewOfRoomRepo;
    public Page<ReviewOfRoom> findByRoomId(int id, Pageable pageable){
        return reviewOfRoomRepo.findByHotelRoomEntityId(id, pageable);
    }
    public double getAverageRating(List<ReviewOfRoom> reviewOfRooms){
        double rating = 0;
        for (ReviewOfRoom reviewOfRoom: reviewOfRooms){
            rating += reviewOfRoom.getRating();
        }
        return rating /= reviewOfRooms.size();
    }
    public List<String> getAverageOfAllRooms(Page<HotelRoomEntity> rooms){
        List<String> averageRatings = new ArrayList<>();
        double averageRating;
        String formattedRating;
        for (HotelRoomEntity room : rooms.getContent()) {
            if(!room.getReviewOfRooms().isEmpty()) {
                averageRating = getAverageRating(room.getReviewOfRooms());
                formattedRating = String.format("%.1f", averageRating);
            } else formattedRating = "5,0";
            averageRatings.add(formattedRating);
        }
        return averageRatings;
    }
    public void saveReview(ReviewOfRoom reviewOfRoom){
        reviewOfRoomRepo.save(reviewOfRoom);
    }

    public List<ReviewOfRoom> findByRoomId(int id){
        return reviewOfRoomRepo.findByHotelRoomEntityId(id);
    }

    public List<ReviewOfRoom> findAll(){
        return reviewOfRoomRepo.findAll();
    }

    @Transactional
    public void deleteReview(int id){
        reviewOfRoomRepo.deleteById(id);
    }

    public ReviewOfRoom findById(int id){
        return reviewOfRoomRepo.findById(id);
    }
}
