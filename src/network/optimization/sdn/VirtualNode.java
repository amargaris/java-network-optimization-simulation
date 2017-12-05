package network.optimization.sdn;

import java.util.ArrayList;

import network.optimization.sdn.bean.VirtualLink;

public class VirtualNode {
	public int cpucount;
	public float cpufreq;
	public int ram;
	public int id;
	public int vlanid;
	private ArrayList<VirtualLink> links;
}
