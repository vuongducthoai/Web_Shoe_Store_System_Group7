package service.Impl;

import dao.IResponseDAO;
import dao.Impl.ResponseDAOImpl;
import dto.ResponseDTO;
import entity.Admin;
import entity.Response;
import entity.Review;
import entity.User;
import service.IResponseService;

public class ResponseServiceImpl implements IResponseService {
    private IResponseDAO responseDAO = new ResponseDAOImpl();
    public boolean addResponse(ResponseDTO responseDTO) {
        try{
            Admin admin = new Admin();
            admin.setUserID(responseDTO.getAdmin().getUserID());
            admin.setFullName(responseDTO.getAdmin().getFullName());

            Review review = new Review();
            review.setReviewID(responseDTO.getReview().getReviewID());


            Response response = new Response();
            response.setResponseID(responseDTO.getResponseID());
            response.setTimeStamp(responseDTO.getTimeStamp());
            response.setContent(responseDTO.getContent());
            response.setAdmin(admin);

            response.setReview(review);
            responseDAO.addResponse(response);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean deleteResponse(int responseID) {return responseDAO.deleteResponse(responseID);}
}
