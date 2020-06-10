package zust.se.edu.sy4.Controller;


import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import zust.se.edu.sy4.Util.QiniuUtil;

import java.io.*;
import java.util.Map;

@Controller
public class QiniuController {


    QiniuUtil util=new QiniuUtil();

    @GetMapping("/upload")
    public String show(){
        return "upload";
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile)throws IOException{
            FileInputStream fileInputStream=(FileInputStream)multipartFile.getInputStream();
            util.upload(fileInputStream);
            return "redirect:list";
    }

    @GetMapping("/list")
    public String getList(Model model) throws QiniuException,UnsupportedEncodingException{
        Map fmap=util.listFiles();
        model.addAttribute("fmap",fmap);
        return "list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("key")String key)throws QiniuException{
        util.deleteFile(key);
        return "redirect:list";
    }

}
