package zust.se.edu.sy4.Util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


public class QiniuUtil {
    private String ACCESS_KEY="BKPSgP9rcC0OtGvEOPxU3a5LNQqrcPgCHroMNNrc";
    private String SECRET_KEY="AckihWrpp-KaNDMwej6VJnfPnwfO2bk7LjtZfPay";
    private String BUCKET_NAME="ryh666";

    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    Zone z=Zone.autoZone();
    Configuration c=new Configuration(z);
    UploadManager uploadManager =new UploadManager(c);
    BucketManager  bucketManager=new BucketManager(auth,c);


    public String getFinalUrl(String key) throws UnsupportedEncodingException{
        String fileName=key;
        String domainOfBucket="http://qb191gk1x.bkt.clouddn.com";
        String encodeFileName= URLEncoder.encode(fileName,"utf-8").replace("+","%20");
        String publicUrl=String.format("%s%s",domainOfBucket,encodeFileName);
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        long expireInseconds=3600;//1小时
        return auth.privateDownloadUrl(publicUrl,expireInseconds);
    }

    public void upload(FileInputStream inputStream)throws QiniuException {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String key= UUID.randomUUID().toString();
        uploadManager.put(inputStream,key,auth.uploadToken(BUCKET_NAME),null,null);
    }

    //文件列表
    public Map listFiles() throws QiniuException,UnsupportedEncodingException{
        FileListing fileListing=bucketManager.listFiles(BUCKET_NAME,null,null,1000,null);
        Map fmap=new HashMap();
        if (fileListing.items !=null){
            for(FileInfo info:fileListing.items){
                System.out.println(info.key);
                fmap.put(info.key,getFinalUrl(info.key));
            }
        }
        return fmap;
    }

    //删除文件
    public String  deleteFile(String key) throws QiniuException{
        bucketManager.delete(BUCKET_NAME,key);
        return "list";
    }

}
