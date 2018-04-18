package com.cntest.fxpt.tree;

import java.util.ArrayList;
import java.util.List;

import com.cntest.common.shiro.RemoteServiceInterface;

public class OrganizationServiceImpl implements OrganizationService {

	//@Autowired
	private RemoteServiceInterface remoteService;
	@Override
	public OrganizationTreeNode getOrgsTree(String appKey, String username) {
		List<String> list = new ArrayList<String>();
		List<Organization> orgList = new ArrayList<Organization>();
//		list = remoteService.getResouces(appKey, username);
		for (int i = 0; i < list.size(); i++) {
			String value = list.get(i);
			String[] resouce = value.split(",");
			Organization organization = new Organization();
			organization.setId(resouce[0]);
			organization.setName(resouce[1]);
			organization.setSn("");
			organization.setDescription(resouce[2]);
			organization.setPid(resouce[3]);
			orgList.add(organization);
		}
		OrganizationTreeNode rootNode = new OrganizationTreeNode();
		rootNode.setId("1");
		rootNode.setName("资源");
		rootNode.setLevel(0);
		rootNode.setLeaf(false);
		if(orgList.size()>0){
			rootNode = this.constructTree(rootNode, orgList, 0);
		}
		return rootNode;
	}
	
	public OrganizationTreeNode constructTree(OrganizationTreeNode rootNode, List<Organization> orgList, int rootLevel){
		List<OrganizationTreeNode> childNodeList = new ArrayList<OrganizationTreeNode>();
		//构造根节点
		for(int i=0; i<orgList.size(); i++){
			Organization org = orgList.get(i);
			if(org.getPid().equals(rootNode.getId())){
				OrganizationTreeNode childNode = new OrganizationTreeNode();
				//copy Organization to OrganizationTreeNode
//				System.out.println(org.getId());
				childNode.setId(org.getId());
				childNode.setName(org.getName());
				childNode.setSn(org.getSn());
				childNode.setDescription(org.getDescription());
				childNode.setParent(rootNode);
				//设置深度
				childNode.setLevel(rootLevel+1);
				childNodeList.add(childNode);
			}
		}
		//设置子节点
		rootNode.setChilds(childNodeList);
		//设置是否叶子节点
		if(childNodeList.size()==0){
			rootNode.setLeaf(true);
		} else {
			rootNode.setLeaf(false);
		}
		//递归构造子节点
		for(int j=0; j<childNodeList.size();j++){
			//进入子节点构造时深度+1
			constructTree(childNodeList.get(j), orgList, ++rootLevel);
			//递归调用返回时，构造子节点的兄弟节点，深度要和该子节点深度一样，因为之前加1，所以要减1
			--rootLevel;
		}
		return rootNode;
	}

}
