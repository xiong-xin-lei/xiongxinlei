package com.bsg.dbscale.dbscale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.dto.AppDTO;
import com.bsg.dbscale.service.service.AppService;

@Controller
@RequestMapping(value = "app")
public class AppController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AppService appService;
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView login(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("/login");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get /login exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/sites", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView site(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/site/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/site/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/sites/details", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView siteNewDetails(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/site/details");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/site/details exception:", e);
		}
		return mv;
	}
	
	@RequestMapping(value = "/sites/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView siteNewAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/site/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/site/add exception:", e);
		}
		return mv;
	}
	
	@RequestMapping(value = "/sites/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView siteNewEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/site/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/site/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView indexNew(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, 0L);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("menuList", JSONObject.toJSONString(result));
			mv.setViewName("views/index");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/index exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/task", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView task(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/task");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/task exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/home/list", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView homeList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			mv.addObject("roleId", userDO.getRoleId());
			mv.setViewName("views/home/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/home/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/home/hostState", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView homeHostState(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			mv.addObject("roleId", userDO.getRoleId());
			mv.setViewName("views/home/hostState");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/home/hostState exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/home/unitState", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView homeUnitState(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			mv.addObject("roleId", userDO.getRoleId());
			mv.setViewName("views/home/unitState");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/home/unitState exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/home/serviceState", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView homeServiceState(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			mv.addObject("roleId", userDO.getRoleId());
			mv.setViewName("views/home/serviceState");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/home/serviceState exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/backup_storage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceBackupStorage(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/backup_storage/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/backup_storage/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/backup_storage/tabNfs/list", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceBackupStorageTabNfs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/backup_storage/tabNfs/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/backup_storage/tabNfs/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/backup_storage/tabNfs/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceBackupStorageTabNfsAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/backup_storage/tabNfs/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/backup_storage/tabNfs/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/business_areas", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceBusinessAreas(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/business_areas/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/business_areas/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/business_areas/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceBusinessAreasAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/business_areas/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/business_areas/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/business_areas/edit/{area_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceBusinessAreasEdit(@PathVariable("area_id") String areaId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("areaId", JSONObject.toJSONString(areaId));
			mv.setViewName("views/resource/business_areas/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/business_areas/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/clusters", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceClusters(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/clusters/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/clusters/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/clusters/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceClustersAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/clusters/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/clusters/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/clusters/edit/{cluster_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceClustersEdit(@PathVariable("cluster_id") String clusterId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("clusterId", JSONObject.toJSONString(clusterId));
			mv.setViewName("views/resource/clusters/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/clusters/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/networks", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceNetworks(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/networks/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/networks/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/networks/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceNetworksAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/networks/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/networks/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/networks/edit/{networks_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceNetworksEdit(@PathVariable("networks_id") String networksId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("networksId", JSONObject.toJSONString(networksId));
			mv.setViewName("views/resource/networks/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/networks/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/images", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceImagesList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/images/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/images/list exception:", e);
		}
		return mv;
	}
	
	@RequestMapping(value = "/resource/images/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceImagesAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/images/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/images/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/images/templates/{type}/{images:.+}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceiImagesTemplate(@PathVariable("type") String type,
			@PathVariable("images") String images, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("type", JSONObject.toJSONString(type));
			mv.addObject("images", JSONObject.toJSONString(images));
			mv.setViewName("views/resource/images/templates");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/images/templates exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/volumepaths", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestorageVolumepaths(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/storage/volumepaths/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/volumepaths/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/volumepaths/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestorageVolumepathsAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/storage/volumepaths/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/volumepaths/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/volumepaths/edit/{remote_storage_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestorageVolumepathsEdit(@PathVariable("remote_storage_id") String remoteStorageId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("remoteStorageId", JSONObject.toJSONString(remoteStorageId));
			mv.setViewName("views/resource/storage/volumepaths/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/volumepaths/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/volumepaths/RGmanage/{remote_storage_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestorageVolumepathsRGmanage(@PathVariable("remote_storage_id") String remoteStorageId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("remoteStorageId", JSONObject.toJSONString(remoteStorageId));
			mv.setViewName("views/resource/storage/volumepaths/RGmanage/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/volumepaths/RGmanage/list:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/volumepaths/RGmanage/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestorageVolumepathsRGmanageAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/storage/volumepaths/RGmanage/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/volumepaths/RGmanage/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/volumepaths/RGmanage/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestorageVolumepathsRGmanageEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/storage/volumepaths/RGmanage/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/volumepaths/RGmanage/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/pvcs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestoragePvcs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/storage/pvcs/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/pvcs/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/pvcs/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestoragePvcsAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/storage/pvcs/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/pvcs/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/storage/pvcs/edit/{remote_storage_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourcestoragePvcsEdit(@PathVariable("remote_storage_id") String remoteStorageId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("remoteStorageId", JSONObject.toJSONString(remoteStorageId));
			mv.setViewName("views/resource/storage/pvcs/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/storage/pvcs/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHosts(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/hosts/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hostsAdd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsAddVue(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/hosts/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/in/{hosts_data}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsInVue(@PathVariable("hosts_data") Boolean hostsData,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("hostsData", JSONObject.toJSONString(hostsData));
			mv.setViewName("views/resource/hosts/hostsIn");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/hostsIn exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/in/identification", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsInVue(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/hosts/identification");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/identification exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/out/{hosts_data}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsOutVue(@PathVariable("hosts_data") Boolean hostsData,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("hostsData", JSONObject.toJSONString(hostsData));
			mv.setViewName("views/resource/hosts/hostsOut");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/hostsOut exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/edit/{hosts_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsEdit(@PathVariable("hosts_id") String hostsId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("hostsId", JSONObject.toJSONString(hostsId));
			mv.setViewName("views/resource/hosts/edit/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/edit/edit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/editLittle/{hosts_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostseditLittle(@PathVariable("hosts_id") String hostsId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("hostsId", JSONObject.toJSONString(hostsId));
			mv.setViewName("views/resource/hosts/edit/editLittle");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/edit/editLittle:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/edit/identification", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostseditLittleIdentification(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/hosts/edit/identification");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/edit/identification:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/incident", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsIncident(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/hosts/incident");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/incident exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/unit/{hosts_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsUnit(@PathVariable("hosts_id") String hostsId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/resource/hosts/unit/unit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/unit/unit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/resource/hosts/unit/rebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView resourceHostsUnitRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/resource/hosts/unit/rebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/resource/hosts/unit/rebuild exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/scales", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessScales(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/business/scales/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/scales/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/scales/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessScalesAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/business/scales/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/scales/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/business_systems", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessBusinessSystems(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/business/business_systems/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/business_systems/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/business_systems/Sysadd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessBusinessSystemsSysadd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/business/business_systems/Sysadd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/business_systems/Sysadd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/business_systems/Sysedit/{Sys_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessBusinessSystemsSysedit(@PathVariable("Sys_id") String SysId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("SysId", JSONObject.toJSONString(SysId));
			mv.setViewName("views/business/business_systems/Sysedit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/business_systems/Sysedit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/business_systems/Subslist/{Sys_ids}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessBusinessSystemsSubsList(@PathVariable("Sys_ids") String SysIds,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("SysIds", JSONObject.toJSONString(SysIds));
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/business/business_systems/Subslist");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/business_systems/Subslist exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/business_systems/Subsadd/{SubsAdd_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessBusinessSystemsSubsadd(@PathVariable("SubsAdd_id") String SubsAddId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("SubsAddId", JSONObject.toJSONString(SubsAddId));
			mv.setViewName("views/business/business_systems/Subsadd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/business_systems/Subsadd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/business/business_systems/Subsedit/{Subs_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView businessBusinessSystemsSubsedit(@PathVariable("Subs_id") String SubsId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("SubsId", JSONObject.toJSONString(SubsId));
			mv.setViewName("views/business/business_systems/Subsedit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/business/business_systems/Subsedit:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/task", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderTask(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/order/task");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/task exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/mysqls/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/order/mysqls/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/approved/new/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlApprovedNew(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/mysqls/approved/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/approved/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/approved/scaleUp/cpumem/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlApprovedScaleUpCpumem(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/mysqls/approved/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/approved/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/approved/scaleUp/storage/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlApprovedScaleUpStorage(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/mysqls/approved/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/approved/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/approved/imageUpdate/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlApprovedImageUpdate(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/mysqls/approved/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/approved/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/approved/delete/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlApprovedDelete(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/mysqls/approved/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/approved/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/execute/new/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlExecuteNew(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/mysqls/execute/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/execute/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/execute/scaleUp/cpumem/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlExecuteScaleUpCpumem(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/mysqls/execute/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/execute/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/execute/scaleUp/storage/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlExecuteScaleUpStorage(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/mysqls/execute/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/execute/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/execute/imageUpdate/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlExecuteImageUpdate(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/mysqls/execute/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/execute/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/execute/delete/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlExecuteDelete(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/mysqls/execute/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/execute/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/edit/new/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlEditNew(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/mysqls/edit/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/edit/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/edit/scaleUp/cpumem/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlEditScaleUpCpumem(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/mysqls/edit/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/edit/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/edit/scaleUp/storage/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlEditScaleUpStorage(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/mysqls/edit/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/edit/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/mysqls/edit/imageUpdate/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderMysqlEditImageUpdate(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/mysqls/edit/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/mysqls/edit/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cmhas/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/order/cmhas/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/approved/new/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaApprovedNew(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/cmhas/approved/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/approved/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/approved/scaleUp/cpumem/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaApprovedScaleUpCpumem(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/cmhas/approved/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/approved/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/approved/scaleUp/storage/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaApprovedScaleUpStorage(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/cmhas/approved/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/approved/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/approved/imageUpdate/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaApprovedImageUpdate(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/cmhas/approved/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/approved/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/approved/delete/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaApprovedDelete(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/cmhas/approved/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/approved/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/execute/new/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaExecuteNew(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/cmhas/execute/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/execute/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/execute/scaleUp/cpumem/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaExecuteScaleUpCpumem(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/cmhas/execute/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/execute/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/execute/scaleUp/storage/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaExecuteScaleUpStorage(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/cmhas/execute/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/execute/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/execute/imageUpdate/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaExecuteImageUpdate(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/cmhas/execute/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/execute/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/execute/delete/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaExecuteDelete(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/cmhas/execute/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/execute/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/edit/new/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaEditNew(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/cmhas/edit/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/edit/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/edit/scaleUp/cpumem/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaEditScaleUpCpumem(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/cmhas/edit/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/edit/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/edit/scaleUp/storage/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaEditScaleUpStorage(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/cmhas/edit/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/edit/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cmhas/edit/imageUpdate/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCmhaEditImageUpdate(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/cmhas/edit/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cmhas/edit/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/redis/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/order/redis/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/approved/new/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisApprovedNew(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/redis/approved/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/approved/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/approved/scaleUp/cpumem/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisApprovedScaleUpCpumem(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/redis/approved/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/approved/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/approved/scaleUp/storage/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisApprovedScaleUpStorage(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/redis/approved/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/approved/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/approved/imageUpdate/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisApprovedImageUpdate(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/redis/approved/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/approved/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/approved/delete/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisApprovedDelete(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/redis/approved/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/approved/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/execute/new/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisExecuteNew(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/redis/execute/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/execute/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/execute/scaleUp/cpumem/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisExecuteScaleUpCpumem(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/redis/execute/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/execute/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/execute/scaleUp/storage/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisExecuteScaleUpStorage(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/redis/execute/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/execute/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/execute/imageUpdate/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisExecuteImageUpdate(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/redis/execute/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/execute/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/execute/delete/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisExecuteDelete(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/redis/execute/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/execute/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/edit/new/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisEditNew(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/redis/edit/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/edit/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/edit/scaleUp/cpumem/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisEditScaleUpCpumem(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/redis/edit/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/edit/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/edit/scaleUp/storage/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisEditScaleUpStorage(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/redis/edit/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/edit/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/redis/edit/imageUpdate/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderRedisEditImageUpdate(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/redis/edit/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/redis/edit/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/apaches/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/order/apaches/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/approved/new/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesApprovedNew(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/apaches/approved/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/approved/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/approved/scaleUp/cpumem/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesApprovedScaleUpCpumem(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/apaches/approved/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/approved/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/approved/scaleUp/storage/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesApprovedScaleUpStorage(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/apaches/approved/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/approved/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/approved/archUp/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesApprovedArchUp(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/apaches/approved/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/approved/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/approved/imageUpdate/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesApprovedImageUpdate(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/apaches/approved/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/approved/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/approved/delete/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesApprovedDelete(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/apaches/approved/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/approved/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/execute/new/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesExecuteNew(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/apaches/execute/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/execute/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/execute/scaleUp/cpumem/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesExecuteScaleUpCpumem(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/apaches/execute/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/execute/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/execute/scaleUp/storage/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesExecuteScaleUpStorage(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/apaches/execute/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/execute/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/execute/archUp/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesExecuteArchUp(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/apaches/execute/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/execute/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/execute/imageUpdate/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesExecuteImageUpdate(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/apaches/execute/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/execute/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/execute/delete/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesExecuteDelete(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/apaches/execute/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/execute/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/edit/new/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesEditNew(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/apaches/edit/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/edit/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/edit/scaleUp/cpumem/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesEditScaleUpCpumem(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/apaches/edit/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/edit/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/edit/scaleUp/storage/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesEditScaleUpStorage(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/apaches/edit/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/edit/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/edit/archUp/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesEditArchUp(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/apaches/edit/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/edit/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/apaches/edit/imageUpdate/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderApachesEditImageUpdate(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/apaches/edit/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/apaches/edit/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxList(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/nginx/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/order/nginx/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/approved/new/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxApprovedNew(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/nginx/approved/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/approved/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/approved/scaleUp/cpumem/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxApprovedScaleUpCpumem(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/nginx/approved/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/approved/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/approved/scaleUp/storage/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxApprovedScaleUpStorage(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/nginx/approved/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/approved/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/approved/archUp/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxApprovedArchUp(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/nginx/approved/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/approved/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/approved/imageUpdate/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxApprovedImageUpdate(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/nginx/approved/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/approved/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/approved/delete/{approved_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxApprovedDelete(@PathVariable("approved_id") String approvedId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("approvedId", JSONObject.toJSONString(approvedId));
			mv.setViewName("views/order/nginx/approved/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/approved/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/execute/new/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxExecuteNew(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/nginx/execute/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/execute/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/execute/scaleUp/cpumem/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxExecuteScaleUpCpumem(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/nginx/execute/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/execute/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/execute/scaleUp/storage/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxExecuteScaleUpStorage(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/nginx/execute/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/execute/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/execute/archUp/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxExecuteArchUp(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/nginx/execute/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/execute/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/execute/imageUpdate/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxExecuteImageUpdate(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/nginx/execute/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/execute/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/execute/delete/{execute_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxExecuteDelete(@PathVariable("execute_id") String executeId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("executeId", JSONObject.toJSONString(executeId));
			mv.setViewName("views/order/nginx/execute/delete");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/execute/delete exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/edit/new/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxEditNew(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/nginx/edit/new");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/edit/new exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/edit/scaleUp/cpumem/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxEditScaleUpCpumem(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/nginx/edit/scaleUpCpumem");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/edit/scaleUpCpumem exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/edit/scaleUp/storage/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxEditScaleUpStorage(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/nginx/edit/scaleUpStorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/edit/scaleUpStorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/edit/archUp/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxEditArchUp(@PathVariable("edit_id") String editId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/nginx/edit/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/edit/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/nginx/edit/imageUpdate/{edit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderNginxEditImageUpdate(@PathVariable("edit_id") String editId,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("editId", JSONObject.toJSONString(editId));
			mv.setViewName("views/order/nginx/edit/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/nginx/edit/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cfgs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCfgs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("roleId", userDO.getRoleId());
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cfgs/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cfgs/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cfgs/mysqlCfgs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCfgMysqlCfgs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cfgs/MySQL");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cfgs/MySQL exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cfgs/cmhaCfgs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCfgsCmhaCfgs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cfgs/CMHA");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cfgs/CMHA exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cfgs/redisCfgs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCfgsRedisCfgs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cfgs/Redis");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cfgs/Redis exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cfgs/apacheCfgs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCfgApacheCfgs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cfgs/Apache");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cfgs/Apache exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/order/cfgs/nginxCfgs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderCfgNginxCfgs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/order/cfgs/Nginx");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/order/cfgs/Nginx exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/incident", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupIncident(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/incident");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/incident exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/incidentUnit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupIncidentUnit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/incidentUnit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/incidentUnit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/usersCheck", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupUsersCheck(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/usersCheck");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/usersCheck exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/webSSH/{unit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupWebSSH(@PathVariable("unit_id") String unitId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/webSSH");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/webSSH exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/logView/{unit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupLogView(@PathVariable("unit_id") String unitId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/logView");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/logView exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/slowLog/{unit_id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupSlowLog(@PathVariable("unit_id") String unitId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/slowLog");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/slowLog exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqls(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/servgroup/mysqls/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/scaleUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsScaleUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/scaleUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/scaleUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/scaleUpstorage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsScaleUpstorage(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/scaleUpstorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/scaleUpstorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/imageUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsImageUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manage/{rowId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManage(@PathVariable("rowId") String rowId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/topology", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabTopology(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		String rowId = request.getParameter("rowId");
		try {
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/topology/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/topology/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysql(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/unit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabUnit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/unitList");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/unitList exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql/rebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysqlRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/rebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/rebuild exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql/rebuildMysql", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysqlRebuildMysql(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/rebuildMysql");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/rebuildMysql exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql/rebuildInit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysqlRebuildInit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/rebuildInit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/rebuildInit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysqlBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql/restore", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysqlRestore(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/restore");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/restore exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/mysql/updateRole", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabMysqlUpdateRole(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/mysql/updateRole");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/mysql/updateRole exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/paramCfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabParamCfg(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/paramCfg/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/paramCfg/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/DB", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabDB(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/DB/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/DB/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/DB/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabDBAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/DB/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/DB/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/DB/table", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabDBTable(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/DB/table");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/DB/table exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/user", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabUser(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/user/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/user/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/user/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabUserAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/user/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/user/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/user/update", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabUserUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/user/update");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/user/update exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/user/resetPwd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabUserResetPwd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/user/resetPwd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/user/resetPwd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/backupStrategy", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabBackupStrategy(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/backupStrategy/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/backupStrategy/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/backupStrategy/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabBackupStrategyAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/backupStrategy/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/backupStrategy/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/backupStrategy/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabBackupStrategyEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/backupStrategy/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/backupStrategy/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/backupFile", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabBackupFile(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/mysqls/manageTab/backupFile/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/backupFile/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/mysqls/manageTab/backupFile/msgList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupMysqlsManageTabBackupFileMsgListe(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/mysqls/manageTab/backupFile/msgList");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/mysqls/manageTab/backupFile/msgList exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhas(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/servgroup/cmhas/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/scaleUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasScaleUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/scaleUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/scaleUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/scaleUpstorage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasScaleUpstorage(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/scaleUpstorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/scaleUpstorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/imageUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasImageUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmha/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhaBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmha/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmha/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manage/{rowId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManage(@PathVariable("rowId") String rowId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/topology", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabTopology(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/topology/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/topology/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/unit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUnit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/unit/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/unit/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/unit/rebuildMysql", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUnitRebuildMysql(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/unit/rebuildMysql");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/unit/rebuildMysql exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/unit/rebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUnitRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/unit/rebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/unit/rebuild exception:", e);
		}
		return mv;
	}
	
	@RequestMapping(value = "/servgroup/cmhas/manageTab/unit/rebuildInit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUnitRebuildInit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/unit/rebuildInit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/unit/rebuildInit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/unit/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhaManageTabUnitBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/unit/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/unit/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/unit/restore", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhaManageTabUnitRestore(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/unit/restore");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/unit/restore exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/paramCfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabParamCfg(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/paramCfg/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/paramCfg/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/DB", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabDB(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/DB/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/DB/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/DB/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabDBAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/DB/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/DB/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/DB/table", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabDBTable(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/DB/table");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/DB/table exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/user", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUser(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/user/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/user/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/user/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhassManageTabUserAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/user/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/user/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/user/update", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUserUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/user/update");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/user/update exception:", e);
		}
		return mv;
	}
	
	@RequestMapping(value = "/servgroup/cmhas/manageTab/user/resetPwd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabUserResetPwd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/user/resetPwd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/user/resetPwd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/backupStrategy", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabBackupStrategy(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/backupStrategy/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/backupStrategy/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/backupStrategy/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabBackupStrategyAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/backupStrategy/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/backupStrategy/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/backupStrategy/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabBackupStrategyEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/backupStrategy/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/backupStrategy/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/backupFile", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabBackupFile(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/cmhas/manageTab/backupFile/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/backupFile/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/cmhas/manageTab/backupFile/msgList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupCmhasManageTabBackupFileMsgListe(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/cmhas/manageTab/backupFile/msgList");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/manageTab/backupFile/msgList exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedis(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/servgroup/redis/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/scaleUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisScaleUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/scaleUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/scaleUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/scaleUpstorage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisScaleUpstorage(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/scaleUpstorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/cmhas/scaleUpstorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/imageUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisImageUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/resetPwd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisResetPwd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/resetPwd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/resetPwd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manage/{rowId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManage(@PathVariable("rowId") String rowId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/redis/manage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/topology", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabTopology(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/redis/manageTab/topology/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/topology/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/unit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabUnit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/redis/manageTab/unit/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/unit/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/unitRedis", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabUnitRedis(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/redis/manageTab/unit/redisList");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/unit/redisList exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/unit/rebuildMysql", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabUnitRebuildMysql(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/manageTab/unit/rebuildMysql");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/unit/rebuildMysql exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/unit/rebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabUnitRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/manageTab/unit/rebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/unit/rebuild exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/unit/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabUnitBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/manageTab/unit/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/unit/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/unit/restore", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupRedisManageTabUnitRestore(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/redis/manageTab/unit/restore");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/unit/restore exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/redis/manageTab/paramCfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupredisManageTabParamCfg(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/redis/manageTab/paramCfg/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/redis/manageTab/paramCfg/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApaches(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/servgroup/apaches/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/scaleUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesScaleUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/scaleUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/scaleUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/archUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesArchUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/scaleUpstorage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesScaleUpstorage(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/scaleUpstorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/scaleUpstorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/imageUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesImageUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/manage/{rowId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesManage(@PathVariable("rowId") String rowId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/apaches/manage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/manage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/manageTab/unit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesManageTabUnit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/apaches/manageTab/unit/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/manageTab/unit/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/manageTab/unit/rebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesManageTabUnitRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/manageTab/unit/rebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/manageTab/unit/rebuild exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/manageTab/unit/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesManageTabUnitBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/manageTab/unit/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/manageTab/unit/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/manageTab/unit/restore", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesManageTabUnitRestore(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/apaches/manageTab/unit/restore");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/manageTab/unit/restore exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/apaches/manageTab/paramCfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupApachesManageTabParamCfg(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/apaches/manageTab/paramCfg/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/apaches/manageTab/paramCfg/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginx(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/servgroup/nginx/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/scaleUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxScaleUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/scaleUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/scaleUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/scaleUpstorage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxScaleUpstorage(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/scaleUpstorage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/scaleUpstorage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/archUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxArchUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/archUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/archUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/imageUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxImageUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/imageUpdate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/imageUpdate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manage/{rowId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManage(@PathVariable("rowId") String rowId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manage");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manage exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/unit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUnit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manageTab/unit/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/unit/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/unit/rebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUnitRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/unit/rebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/unit/rebuild exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/unit/backUp", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUnitBackUp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/unit/backUp");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/unit/backUp exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/unit/restore", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUnitRestore(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/unit/restore");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/unit/restore exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/paramCfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabParamCfg(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manageTab/paramCfg/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/paramCfg/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/DB", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabDB(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manageTab/DB/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/DB/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/DB/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabDBAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/DB/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/DB/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/DB/table", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabDBTable(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/DB/table");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/DB/table exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/user", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUser(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manageTab/user/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/user/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/user/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUserAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/user/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/user/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/user/update", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabUserUpdate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/user/update");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/user/update exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/backupStrategy", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabBackupStrategy(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manageTab/backupStrategy/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/backupStrategy/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/backupStrategy/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabBackupStrategyAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/backupStrategy/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/backupStrategy/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/backupStrategy/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabBackupStrategyEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/backupStrategy/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/backupStrategy/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/backupFile", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabBackupFile(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		String rowId = request.getParameter("rowId");

		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/servgroup/nginx/manageTab/backupFile/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/backupFile/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/servgroup/nginx/manageTab/backupFile/msgList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView servgroupNginxManageTabBackupFileMsgListe(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/servgroup/nginx/manageTab/backupFile/msgList");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/servgroup/nginx/manageTab/backupFile/msgList exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/users", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemUsers(HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDO userDO = (UserDO) session.getAttribute("user");
		Boolean manager = userDO.getRole().getManager();
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			result = appService.list(userDO, id);

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			if (manager) {
				mv.setViewName("views/system/users/list");
			} else {
				mv.setViewName("views/system/users/userList");
			}
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			if (manager) {
				logger.error("get views/system/users/list exception:", e);
			} else {
				logger.error("get views/system/users/userList exception:", e);
			}
		}
		return mv;
	}

	@RequestMapping(value = "/system/users/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemUsersAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/users/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/users/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/users/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemUsersEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/users/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/users/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/users/updatePwd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemUsersUpdatePwd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/users/updatePwd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/users/updatePwd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/groups", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemGroups(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/system/groups/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/groups/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/groups/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemGroupsAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/groups/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/groups/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/groups/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemGroupsEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/groups/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/groups/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/groups/user", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemGroupsUser(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/groups/user");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/groups/user exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/groups/userAdd", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemGroupsUserAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/groups/userAdd");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/groups/userAdd exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/roles", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemRoles(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/system/roles/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/roles/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/roles/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemRolesAdd(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/roles/add");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/roles/add exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/roles/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemRolesEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/roles/edit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/roles/edit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/roles/privilege/{rowId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemRolesPrivilege(@PathVariable("rowId") String rowId, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);
			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.addObject("rowId", JSONObject.toJSONString(rowId));
			mv.setViewName("views/system/roles/privilege");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/roles/privilege exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/roles/privilegeTab/app", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemRolesPrivilegeTabApp(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/roles/privilegeTab/app");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/roles/privilegeTab/app exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/tasks", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemTasks(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/system/tasks/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/tasks/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/tasks/taskStatus", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemTasksTaskStatus(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/tasks/taskStatus");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/tasks/taskStatus exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/tasks/taskEdit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemTasksTaskEdit(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/tasks/taskEdit");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/tasks/taskEdit exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/operatelogs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemOperatelogs(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		List<AppDTO> result = new ArrayList<>();
		Long id = Long.parseLong(request.getParameter("id"));
		try {
			HttpSession session = request.getSession();
			UserDO userDO = (UserDO) session.getAttribute("user");
			result = appService.list(userDO, id);

			mv.addObject("btnPer", JSONObject.toJSONString(result));
			mv.setViewName("views/system/operatelogs/list");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/operatelogs/list exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/operatelogs/operate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemOperatelogsOperate(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/operatelogs/operate");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/operatelogs/operate exception:", e);
		}
		return mv;
	}

	@RequestMapping(value = "/system/operatelogs/forceRebuild", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemOperatelogsForceRebuild(HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("views/system/operatelogs/forceRebuild");
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error("get views/system/operatelogs/forceRebuild exception:", e);
		}
		return mv;
	}

}
