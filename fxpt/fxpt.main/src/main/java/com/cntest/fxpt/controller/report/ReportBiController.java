package com.cntest.fxpt.controller.report;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.service.IReportExamService;
import com.cntest.fxpt.bi.BIConnectorPoolMgr;
import com.cntest.fxpt.bi.BIOperater;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.service.IApplySetService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.ShiroClientConfig;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.security.DefaultUserResource;
import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;
import com.cntest.security.remote.UserDetailsService;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

@Controller
@RequestMapping("/report")
public class ReportBiController extends BaseController {
  private static Logger logger = LoggerFactory.getLogger(ReportBiController.class);
  // @Autowired(required = false)
  // @Qualifier("A4Service")
  // private A4Service a4Service;
  // @Autowired
  // private RemoteServiceInterface remoteService;
  //
  // @Autowired
  // private OrganizationService organizationService;

  // @Autowired(required = false)
  // private IUserResourceService userResourceService;

  @Autowired(required = false)
  @Qualifier("bi.BIConnectorPoolMgr")
  private BIConnectorPoolMgr biConnectorPoolMgr;


  @Autowired(required = false)
  @Qualifier("bi.BIOperater")
  private BIOperater biOperater;

  @Autowired(required = false)
  private IReportExamService reportExamService;

  @Autowired(required = false)
  private UserDetailsService userDetailsService;

  @Autowired(required = false)
  private UserService userService;

  @Autowired
  private IApplySetService applySetService;


  @Autowired(required = false)
  @Qualifier("IExamService")
  private IExamService examService;

  @RequestMapping("/home/{examId}")
  public ModelAndView home(@PathVariable Long examId) throws Exception {
    String status = "失败", info = "", erre = "";
    Exam exams = reportExamService.getExam(examId);
    try {
      UserDetails user =
          userDetailsService.findUserDetailsBy(userService.getCurrentLoginedUser().getUserName());
      // BIConnector biConnector = biConnectorPoolMgr.getBIConnector();
      String biServerUrl = biOperater.biServerUrl(user.getUserName());
      BiUser biUser = biOperater.getBiUser(user.getUserName());
      UserResource myApp = userDetailsService.getDefaultApp(user);
      // 查询报告数据权限
      // List<UserResource> dataPermisions = userDetailsService.findUserResource(user,
      // IUserResourceService.Type.DATA,
      // IUserResourceService.Level.ALL);
      // List<Exam> sameTermExams =
      // reportExamService.getSameTermExams(user,examService.findById(examId));
      List<Exam> sameTermExams = new ArrayList<Exam>();
      sameTermExams.add(exams);
      List<UserOrg> userOrgs = userDetailsService.findUserOrgs(user);
      List<UserResource> modules = userDetailsService.findUserResourcefilter(user,
          IUserResourceService.Type.MODULE, IUserResourceService.Level.ALL, examId);
      // 将考试名称写入到菜单的首个子菜单中
      ArrayList<UserResource> menus = new ArrayList<UserResource>();
      if (sameTermExams != null && modules != null && modules.size() > 0) {
        for (Exam exam : sameTermExams) {
          UserResource menu = modules.get(0);
          menu.setEventCode(exam.getId() + "");
          UserResource topMenu = menu.clone();
          topMenu.setName(exam.getName());
          menus.add(topMenu);
        }
        addurltoresource(menus.get(0), biServerUrl, biUser);
      }
      // 获取系统退出路径
      Properties prop = new Properties();
      InputStream fis = ShiroClientConfig.class
          .getResourceAsStream("/properties/shiro-client-default.properties");
      prop.load(fis);
      // 一定要在修改值之前关闭fis
      fis.close();
      String sysLoginOut = (String) prop.get("shiro.sso.loginOut.url");
      List<ApplySet> applySeList = applySetService.findApplyByStatus("1");
      String title = "";
      String logoImage = "";
      if (applySeList.size() > 0) {
        title = applySeList.get(0).getSystemName();
        logoImage = applySeList.get(0).getLoginIcon();
        if (logoImage != "") {
          logoImage =
              "style='background: no-repeat center; background-image:url(" + logoImage + ")'";
        }
      } else {
        // 黄洪成2015-03-25 新增关于（系统版本）
        title = SystemConfig.newInstance().getValue("cntest.title");
      }
      // 黄洪成2015-03-25 新增关于（系统版本）
      String version = SystemConfig.newInstance().getValue("cntest.version");
      String date = SystemConfig.newInstance().getValue("cntest.date");
      // ------
      User user_ = userService.findUserBy(user.getUserName());
      status = "成功";

      return ModelAndViewBuilder.newInstanceFor("/report/home")
          // .append("exam", exam)
          .append("exams", sameTermExams).append("app", myApp).append("orgs", userOrgs)
          .append("roleTypes", user.getRoleTypeCode()).append("menus", menus)
          // .append("biCookie", biConnector.getCookie())
          // .append("datas", dataPermisions)
          .append("user", user_).append("sysLoginOut", sysLoginOut).append("logoImage", logoImage)
          .append("priview", false).append("title", title).append("version", version)
          .append("date", date).append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE)
              .code("fxpt.report.home").msg(myApp.getName()).build())
          .build();

    } catch (Exception e) {
      erre = LogUtil.e(e);
      throw e;
    } finally {
      info = "查看报告<b style='color:red;'>" + exams.getName() + "</b>" + status;
      LogUtil.log("报告管理>报告列表", "查看报告", exams.getName(), status, info, erre);
    }

  }


  private void addurltoresource(UserResource menu, String biServerUrl, BiUser biUser) {
    if (menu.isHasChild()) {
      Collection<UserResource> children = menu.getChildren();
      if (children.size() > 0) {
        for (UserResource userResource : children) {
          if (userResource.isHasChild()) {
            addurltoresource(userResource, biServerUrl, biUser);
          } else {
            DefaultUserResource tmp = (DefaultUserResource) userResource;
            if (userResource.getUrl().startsWith("api/repos")) {
              String newUrl = biServerUrl + "/" + userResource.getUrl();
              newUrl += "?userid=" + biUser.getUserName();
              newUrl += "&password=" + biUser.getUserPassword();
              tmp.setUrl(newUrl);
            }
          }
        }

      }
    }
  }

  @RequestMapping("/menu")
  public ModelAndView menu() throws Exception {
    logger.debug("URL /report/home");
    String appKey = getAppKey();
    UserDetails user =
        userDetailsService.findUserDetailsBy(userService.getCurrentLoginedUser().getUserName());
    // 查询报告模块
    List<UserResource> modules = userDetailsService.findUserResource(user,
        IUserResourceService.Type.MODULE, IUserResourceService.Level.ALL);

    logger.debug("URL /report/home");
    return ModelAndViewBuilder.newInstanceFor("/report/menu").append("menus", modules)

        .append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE)
            .code("fxpt.report.home").msg("报告模板读取成功").build())
        .build();
  }

  // @RequestMapping("/reportMenu")
  // public ModelAndView goindex3(String userId) {
  // List<String> list = new ArrayList<String>();
  // String appKey = "645ba616-370a-43a8-a8e0-993e7a590cf0";
  // String username = "";
  // if (userId.equals("1")) {
  // username = "admin";
  // } else if (userId.equals("2")) {
  // username = "zhenshi";
  // } else {
  // username = "tom";
  // }
  // // list = remoteService.getResouces(appKey,username);
  // OrganizationTreeNode node = organizationService.getOrgsTree(appKey,
  // username);
  // List<A4Resources> listA4UserMenus = a4Service
  // .getA4UserResourceMenus(list);
  // ModelAndView modelAndView = new ModelAndView("reportMenu");
  // modelAndView.addObject("data", getDataTime());
  // modelAndView.addObject("listA4UserMenus", listA4UserMenus);
  // modelAndView.addObject("node", node);
  // return modelAndView;
  // }
  //
  // @RequestMapping("/report")
  // public ModelAndView report(String userId) {
  // A4User a4User = new A4User();
  // a4User = a4Service.getUserAndMenu(userId);
  // ModelAndView modelAndView = new ModelAndView("report");
  // modelAndView.addObject("a4User", a4User);
  // modelAndView.addObject("data", getDataTime());
  // return modelAndView;
  // }
  //
  // @RequestMapping("/table-example")
  // public ModelAndView example() {
  // ModelAndView modelAndView = new ModelAndView("table-example");
  // modelAndView.addObject("data", getDataTime());
  // return modelAndView;
  // }
  //
  // public String getDataTime() {
  // // 获得系统当前年月日
  // Calendar calendar = Calendar.getInstance();
  // int year = calendar.get(Calendar.YEAR);
  // int month = calendar.get(Calendar.MONTH) + 1;
  // int day = calendar.get(Calendar.DAY_OF_MONTH);
  // String data = year + "年" + month + "月" + day + "日";
  // return data;
  // }

}
