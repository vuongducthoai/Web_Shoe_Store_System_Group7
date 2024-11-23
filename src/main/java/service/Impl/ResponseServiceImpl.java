package service.Impl;

import dao.IResponseDAO;
import dao.Impl.ResponseDAOImpl;
import dto.ResponseDTO;
import entity.Response;
import entity.Review;
import entity.User;
import service.IResponseService;

public class ResponseServiceImpl implements IResponseService {
    private IResponseDAO responseDAO = new ResponseDAOImpl();
    public boolean addResponse(ResponseDTO responseDTO) {
        try{
            User user = new User();
            user.setUserID(responseDTO.getAdmin().getUserID());

            Review review = new Review();
            review.setReviewID(responseDTO.getReview().getReviewID());


            Response response = new Response();
            response.setResponseID(responseDTO.getResponseID());
            response.setTimeStamp(responseDTO.getTimeStamp());
            response.setContent(responseDTO.getContent());
            response.setAdmin(user);
            response.setReview(review);
            responseDAO.addResponse(response);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
