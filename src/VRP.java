import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.util.*;


public class VRP {
	
	//================================================================
	//							ATRIBUTES 
	//================================================================
	//SETS 
	public List<Integer>N=new ArrayList<Integer>(); //Set of nodes
	public Set<Integer>C=new HashSet<Integer>(); //Set of connected nodes to the distribution center
	public Set<Integer>NC=new HashSet<Integer>(); //Set of nodes that are not connected
	
	//PARAMETERS
	public Double c[][]; //Distance between nodes
	public List<Integer>nc=new ArrayList<Integer>(); //Organized list of nodes in set NC
	public Integer contrNum;
	//VARIABLES
	public GRBVar[][]x;
	
	public VRP( Double c[][]){
		this.c=c;
		contrNum=0;
	}
	//================================================================
	//							METHODS 
	//================================================================

	//Initializations 
	
	public void initialize(){
	
	}	
	
	//Procedure that runs the LP 
	public List<Integer> generateModel(int center){
		List<Integer>sol=new ArrayList<Integer>();
		GRBEnv env;
		try {
			env = new GRBEnv("mip1.log");
			GRBModel  model = new GRBModel(env);
			x=new GRBVar[c.length][c.length];
			for(int i=0;i<c.length;i++){
				for(int j=0;j<c.length;j++){
					if (i!=j) {
						x[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.INTEGER, "x"+i+","+j);
					}
				}		
			}
			model.update();
			
			//Objective Function
			GRBLinExpr expr = new GRBLinExpr();
			for (int i = 0; i < c.length; i++) {
				for (int j = 0; j < c.length; j++) {
					if (i!=j) {
						expr.addTerm(c[i][j], x[i][j]);
					}
					
				}
			}
			model.setObjective(expr, GRB.MINIMIZE);
			
			//Group of Constraints 1: Every node is visited
			for (int i = 0; i < c.length; i++) {
				expr = new GRBLinExpr();
				for (int j = 0; j < c.length; j++) {
					if (i!=j) {
						expr.addTerm(1.0, x[i][j]);
					}
									
				}
				model.addConstr(expr, GRB.EQUAL, 1, "c_1_"+i);				
			}			
			
			//Group of Constraints 2: Every node is visited
			for (int i = 0; i < c.length; i++) {
				expr = new GRBLinExpr();
				for (int j = 0; j < c.length; j++) {
					if (i!=j) {
						expr.addTerm(1.0, x[j][i]);
					}			
				}
				model.addConstr(expr, GRB.EQUAL, 1, "c_2_"+i);				
			}			
			model.optimize();
			model.write("model.lp") ;
			for (int i = 0; i < c.length; i++) {
				for (int j = 0; j < c.length; j++) {
					if(i!=j && x[i][j].get(GRB.DoubleAttr.X)>0.0){
						//System.out.println(i+"->"+j);					
				}
				}
			}
			//*********************************************************************
			boolean yeah_baby=true;
			int count = 1;
			while(yeah_baby){
				
				if (cycles(center).size()==c.length) {
					yeah_baby=false;
					break;
				}
				//System.out.println("***************************");
				//System.out.println( cycles(center).size()==c.length);
				count++;
				newConstraints(center, model);
				model.optimize();
				//System.out.println("***************************");
				//System.out.println( cycles(center).size()==c.length);
				
				
//				model.write("model"+count+".lp");
//				//System.out.println("***************************");	
//				for (int i = 0; i < c.length; i++) {
//					for (int j = 0; j < c.length; j++) {
//						if(i!=j && x[i][j].get(GRB.DoubleAttr.X)>0.0){
//							//System.out.println(i+"->"+j);					
//						}
//					}
//				}
				
				
				//================***************=============
//				count++;
//				newConstraints(center, model);
//				model.optimize();
//				
//				System.out.println("***************************");
//				System.out.println( cycles(center).size()==c.length);
//				
//				
//				model.write("model"+count+".lp");
//				System.out.println("***************************");	
//				for (int i = 0; i < c.length; i++) {
//					for (int j = 0; j < c.length; j++) {
//						if(i!=j && x[i][j].get(GRB.DoubleAttr.X)>0.0){
//							System.out.println(i+"->"+j);					
//						}
//					}
//				}
				//================***************=============

			}
			sol= cycles(center);
			sol.add(0, center);
			//System.out.println("ROUTE: "+sol+ "	Iterations: "+count);
			//*********************************************************************
			
			model.dispose();
			env.dispose();	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		return sol;
	}
	

	//Procedure to add constraints on non-connected nodes
	public void newConstraints(int center, GRBModel mod){ 
		//Initialize the set of nodes
		N=new ArrayList<Integer>();
		for (int i = 0; i < c.length; i++) {
			N.add(i);			
		}
//		System.out.println("N: "+N);//----------------------

		C =  new HashSet<Integer>(cycles(center));
		Set<Integer>NC =  new HashSet<Integer>(N);
		NC.removeAll(C);
//		System.out.println("NC: "+NC);//----------------------
		nc=new ArrayList<Integer>();
		nc.addAll(NC);
//		System.out.println("nc: "+nc);//----------------------
		for (int i = 0; i < nc.size(); i++) {
			if(NC.contains(nc.get(i))){
				List<Integer> temp=new ArrayList<Integer>(cycles(nc.get(i)));

				GRBLinExpr expr = new GRBLinExpr();
				for (int i2 = 0; i2 < temp.size(); i2++) {					
					for (int j2 = 0; j2 < temp.size(); j2++) {
						if (temp.get(i2)!=temp.get(j2)) {
							expr.addTerm(1.0, x[temp.get(i2)][temp.get(j2)]);

						}

					}
				}
				try {
					contrNum++;
					mod.addConstr(expr, GRB.LESS_EQUAL, temp.size()-1, "c_n"+contrNum);
				
				} catch (GRBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//					System.out.println("Restricción:"+expr);//----------------------

			}
		}
	}


	
	
	//Function that returns a list with the nodes that are connected to a node as parameter
	public List<Integer> cycles(int num){
		List<Integer>M=new ArrayList<Integer>();
		boolean cont = true;
		Integer ii = num;

		while (cont) {
			for (int j = 0; j<c.length; j++) {
				try {
					if (ii!=j && x[ii][j].get(GRB.DoubleAttr.X)>0.0) {
						ii=j;
						M.add(j);
						if (j==num) {
							cont=false;
							break;
						}
					}
				} catch (GRBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		System.out.println("M: "+num+" "+M);
		return M;

	}
	
	
	//================================================================
	//							MAIN
	//================================================================

//	public static void main(String[] args) {
//		double time= System.currentTimeMillis();
//		VRP e= new VRP();
//		e.generateModel(0);
//		double time2=(System.currentTimeMillis());
//		System.out.println(time2-time);
//
//	
//	}

}
