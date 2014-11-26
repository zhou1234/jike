package com.jike.shanglv.Update;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseXmlService {
	public HashMap<String, String> parseXml(InputStream inStream)
			throws Exception {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		// ʵ����һ���ĵ�����������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// ͨ���ĵ�������������ȡһ���ĵ�������
		DocumentBuilder builder = factory.newDocumentBuilder();
		// ͨ���ĵ�ͨ���ĵ�����������һ���ĵ�ʵ��
		Document document = builder.parse(inStream);
		// ��ȡXML�ļ����ڵ�
		Element root = document.getDocumentElement();
		// ��������ӽڵ�
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			// �����ӽڵ�
			Node childNode = childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				// �汾��
				if ("version".equals(childElement.getNodeName())) {
					hashMap.put("version", childElement.getFirstChild()
							.getNodeValue());
				}
				// �������
				else if (("name".equals(childElement.getNodeName()))) {
					hashMap.put("name", childElement.getFirstChild()
							.getNodeValue());
				}
				// ���ص�ַ
				else if (("url".equals(childElement.getNodeName()))) {
					hashMap.put("url", childElement.getFirstChild()
							.getNodeValue());
				}
			}
		}
		return hashMap;
	}

	/**
	 * ��ȡ������½ڵ����Ϣ ����infoΪ��ȡ����xml�ļ��ַ�����nameOfNodeΪ���½ڵ��name���ԣ���ʶ�����
	 */
	public static UpdateNode getNodeFromXml(String info, String nameOfNode) {
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(info.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		List<UpdateNode> nodeList = new ArrayList<UpdateNode>();
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		// �����ҵ�xml�ļ�
		factory = DocumentBuilderFactory.newInstance();
		try {
			// �ҵ�xml���������ĵ�
			builder = factory.newDocumentBuilder();
			document = builder.parse(inputStream);
			// �ҵ���Element
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("soft");
			// �������ڵ������ӽڵ�
			UpdateNode node = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				try {
					node = new UpdateNode();
					Element softElement = (Element) (nodes.item(i));
					// ��ȡname����ֵ
					node.setName(softElement.getAttribute("name"));
					try {
						Element versionCode = (Element) softElement
								.getElementsByTagName("versionCode").item(0);
						node.setVersionCode(Integer.valueOf(versionCode
								.getFirstChild().getNodeValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element version = (Element) softElement
								.getElementsByTagName("version").item(0);
						node.setVersion(version.getFirstChild().getNodeValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element download_url = (Element) softElement
								.getElementsByTagName("download_url").item(0);
						node.setDownload_url(download_url.getFirstChild()
								.getNodeValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element content = (Element) softElement
								.getElementsByTagName("content").item(0);
						node.setContent(content.getFirstChild().getNodeValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element updatetime = (Element) softElement
								.getElementsByTagName("updatetime").item(0);
						node.setUpdatetime(updatetime.getFirstChild()
								.getNodeValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element hotelcity = (Element) softElement
								.getElementsByTagName("hotelcity").item(0);
						node.setHotelcity(Integer.valueOf(hotelcity
								.getFirstChild().getNodeValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element flightcity = (Element) softElement
								.getElementsByTagName("flightcity").item(0);
						node.setFlightcity(Integer.valueOf(flightcity
								.getFirstChild().getNodeValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element iflightcity = (Element) softElement
								.getElementsByTagName("iflightcity").item(0);
						node.setIflightcity(Integer.valueOf(iflightcity
								.getFirstChild().getNodeValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element traincity = (Element) softElement
								.getElementsByTagName("traincity").item(0);
						node.setTraincity(Integer.valueOf(traincity
								.getFirstChild().getNodeValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element softname = (Element) softElement
								.getElementsByTagName("softname").item(0);
						node.setSoftname(softname.getFirstChild()
								.getNodeValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					nodeList.add(node);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).getName().equals(nameOfNode)) {
				return nodeList.get(i);
			}
		}
		return null;
	}

}
