
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

 public boolean isTimeWithInRange(LocalTime timeNow,LocalTime timeStart,LocalTime timeEnd){
  return timeNow.isAfter(timeStart) && timeNow.isBefore(timeEnd);
 }

 public boolean isPeakHour(LocalTime timeNow){
  return isTimeWithInRange(timeNow, LocalTime.of(7, 59, 59), LocalTime.of(10, 00, 01)) 
  || isTimeWithInRange(timeNow, LocalTime.of(12, 59, 59), LocalTime.of(14, 00, 01))
  || isTimeWithInRange(timeNow, LocalTime.of(18, 59, 59), LocalTime.of(21, 00, 01));  
 }
  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
        List<Restaurant> restaurants;
        // int hours=currentTime.getHour();
        // int minutes=currentTime.getMinute();
      //   if((hours>=8 && hours<=9) || (hours==10 && minutes==0) 
      //   || (hours==13) || (hours==14 && minutes==0)
      //   || (hours>=19 && minutes<=20) || (hours==21 && minutes==0 )){
      //   restaurants=restaurantRepositoryService.
      //   findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), 
      //   getRestaurantsRequest.getLongitude(), currentTime, peakHoursServingRadiusInKms);
      // }else{
      //   restaurants=restaurantRepositoryService.
      //   findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), 
      //   getRestaurantsRequest.getLongitude(), currentTime, normalHoursServingRadiusInKms);
      // }

      Double servingRadius=isPeakHour(currentTime)?peakHoursServingRadiusInKms:normalHoursServingRadiusInKms;
      restaurants=restaurantRepositoryService.
        findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), 
        getRestaurantsRequest.getLongitude(), currentTime, servingRadius);

      GetRestaurantsResponse response=new GetRestaurantsResponse(restaurants);
     return response;
  }


}

