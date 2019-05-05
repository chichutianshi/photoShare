package com.cust.service;

import com.cust.Entity.Allphotos;
import com.cust.dao.AllphotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

@Service
public class UserSearchService {
    @Autowired
    public AllphotosMapper allphotosMapper;


    public List<Map> selectAllPhotos(int selectRow) {
        List<Map> list = allphotosMapper.randomSelect(selectRow);
        String photoURL;
        String[] URL;
        if (list != null) {
            System.out.println(list);
            for (Map aList : list) {
                photoURL = (String) aList.get("photoURL");//https://www.xqdiary.top
                URL = photoURL.split(";");//http://localhost:8080/
                aList.put("photoURL", "http://localhost:8080/loadPic/"+aList.get("photoId")+"/compress/" + URL[0]);
            }
            return list;
        }
        System.out.println("空");
        return null;
    }

    public String getPhotoUrl(String photoId) {
        String photoUrl = allphotosMapper.getPhotoUrlByphotoId(photoId);
        return photoUrl;
    }

    public List getPicList(String userID) {
        List list=allphotosMapper.getPicListByUserID(userID);
        return list;
    }

    public boolean delPic(String photoId){
        return allphotosMapper.delPicById(photoId)>0;
    }

}
