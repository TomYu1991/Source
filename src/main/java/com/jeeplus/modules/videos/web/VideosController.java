/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.videos.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.videos.entity.Videos;
import com.jeeplus.modules.videos.service.VideosService;

/**
 * 视频配置管理Controller
 *
 * @author zhanglumeng
 * @version 2019-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/videos/videos")
public class VideosController extends BaseController {

    @Autowired
    private VideosService videosService;

    @Autowired
    private WorkStationService workStationService;

    @ModelAttribute
    public Videos get(@RequestParam(required = false) String id) {
        Videos entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = videosService.get(id);
        }
        if (entity == null) {
            entity = new Videos();
        }
        return entity;
    }

    /**
     * 视频配置管理列表页面
     */
    @RequiresPermissions("videos:videos:list")
    @RequestMapping(value = {"list", ""})
    public String list(Videos videos, Model model) {
        model.addAttribute("videos", videos);
        WorkStation w = new WorkStation();

        List<WorkStation> list = workStationService.findList(w);
        model.addAttribute("list", list);
        return "modules/videos/videosList";
    }

    /**
     * 视频配置管理列表数据
     */
    @ResponseBody
    @RequiresPermissions("videos:videos:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Videos videos, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Videos> page = videosService.findPage(new Page<Videos>(request, response), videos);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑视频配置管理表单页面
     */
    @RequiresPermissions(value = {"videos:videos:view", "videos:videos:add", "videos:videos:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Videos videos, Model model) {
        model.addAttribute("videos", videos);
        return "modules/videos/videosForm";
    }

    /**
     * 保存视频配置管理
     */
    @ResponseBody
    @RequiresPermissions(value = {"videos:videos:add", "videos:videos:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(Videos videos, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(videos);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        videos.setFrames("0");
        videos.setInputer(UserUtils.getUser().getName().toString());//录入人
        videos.setInputDate(DateUtils.getDate());//录入时间
        videos.setFlag("1");

        videosService.save(videos);//保存
        j.setSuccess(true);
        j.setMsg("保存视频配置管理成功");
        return j;
    }

    /**
     * 删除视频配置管理
     */
    @ResponseBody
    @RequiresPermissions("videos:videos:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(Videos videos) {
        AjaxJson j = new AjaxJson();
        videosService.delete(videos);
        j.setMsg("删除视频配置管理成功");
        return j;
    }

    /**
     * 批量删除视频配置管理
     */
    @ResponseBody
    @RequiresPermissions("videos:videos:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            videosService.delete(videosService.get(id));
        }
        j.setMsg("删除视频配置管理成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("videos:videos:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Videos videos, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "视频配置管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Videos> page = videosService.findPage(new Page<Videos>(request, response, -1), videos);
            new ExportExcel("视频配置管理", Videos.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出视频配置管理记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("videos:videos:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Videos> list = ei.getDataList(Videos.class);
            for (Videos videos : list) {
                try {
                    videosService.save(videos);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条视频配置管理记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条视频配置管理记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入视频配置管理失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入视频配置管理数据模板
     */
    @ResponseBody
    @RequiresPermissions("videos:videos:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "视频配置管理数据导入模板.xlsx";
            List<Videos> list = Lists.newArrayList();
            new ExportExcel("视频配置管理数据", Videos.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 通过工作站id查询视频信息
     *
     * @param dbid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getvideo")
    public AjaxJson getvideo(String dbid) throws Exception {
        AjaxJson j = new AjaxJson();
        List<Videos> videos = videosService.getVediosByDbid(dbid);
        Videos vs = new Videos();
        try {
            if (videos.size() > 0) {
                vs.setCameraIp(videos.get(0).getCameraIp());
                vs.setCameraPort(videos.get(0).getCameraPort());
                vs.setUsername(videos.get(0).getUsername());
                vs.setPassword(videos.get(0).getPassword());
                vs.setCameraOrder(videos.get(0).getCameraOrder());
                vs.setStationIp(videos.get(0).getStationIp());

                StringBuffer videoCamera = new StringBuffer();
                Videos videoCameras;
                for (int i = 0; i < videos.size(); i++) {
                    videoCameras = videos.get(i);
                    if (i == 0) {
                        videoCamera.append(videoCameras.getVideoCamera());
                    } else {
                        videoCamera.append("," + videoCameras.getVideoCamera());
                    }
                }
                vs.setVideoCamera(videoCamera.toString());
            }

            j.setSuccess(true);
            j.setData(vs);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
        }

        return j;

    }

    /**
     * 通过采集仪IP查询通道号
     */
    @ResponseBody
    @RequestMapping(value = "queryVideoCamera")
    public AjaxJson queryVideoCamera(String stationIp, HttpServletRequest request) throws Exception {
        AjaxJson j = new AjaxJson();
        Videos v = new Videos();
        List<Videos> video = new ArrayList<>();
        j.setMsg(request.getRemoteAddr());
        try {
            if (!"".equals(stationIp) && stationIp != null) {
                video = videosService.queryVideoCamera(stationIp);
            } else {
                String ip = StringUtils.getRemoteAddr(request);
                video = videosService.queryVideoCamera(ip);
            }
            if (video.size() > 0) {
                StringBuffer videoCamera = new StringBuffer();
                Videos videoCameras;
                for (int i = 0; i < video.size(); i++) {
                    videoCameras = video.get(i);
                    if (i == 0) {
                        videoCamera.append(videoCameras.getCameraNo());
                    } else {
                        videoCamera.append("#" + videoCameras.getCameraNo());
                    }
                }

                v.setVideoCamera(videoCamera.toString());
            }

            j.setSuccess(true);
            j.setData(v);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
        }

        return j;

    }
}