clear all 
clc 

swarm_size=30; 
iterations = 30;
 wi=80;
 hi=60;
c1=2.2;
c2=2.2;
 vmax=25;
W = 2.11;
correction_factor = 2.0;
ConstantValue=10;
N=1; % size of each particle.... 
wh=50;

   data=10*randn(2500,2);
% load Data
% data=DataVector;
N=50;%% number of clusters.... in particle 
wh = size(data,2);  %% data in each cluster 
   %globalBest = struct('particle',randn(N,1) , 'vel', randn(N,1) , 'eval', 0.0);
globalBest = struct('particle',randn(wh,N)*ConstantValue, 'vel', randn(wh,N) , 'eval', 0.0,'FincalClusters',zeros(wh,N),'elements',zeros(wh,N));
[globalBestEval,finalC]=evalParticle(globalBest,data);

 for i = 1 : swarm_size
     
  swarm(i).gbest = globalBest; 
 swarm(i).gbest.eval =globalBestEval;
  

  swarm(i).lbest = struct('particle',randn(wh,N)*ConstantValue , 'vel', randn(wh,N) , 'eval', 0.0,'FincalClusters',zeros(wh,N),'elements',zeros(wh,N));
  [swarm(i).lbest.eval,ffff]=evalParticle( swarm(i).lbest,data);
    
    swarm(i).current = struct('particle',randn(wh,N)*ConstantValue , 'vel', randn(wh,N) , 'eval', 0.0,'FincalClusters',zeros(wh,N),'elements',zeros(wh,N)); 
    [swarm(i).current.eval,fff]=evalParticle( swarm(i).current,data);
 end

 
 
 
 
 
   for iter = 1 : iterations    % major swarm iteration... 
   iter 
  for i = 1 : swarm_size
      
       %-- FIRST evaluate position
     [eval,finalC]=evalParticle(swarm(i).current,data);
     swarm(i).current.eval=eval ;
     localBestEval=swarm(i).lbest.eval;

    % globalBestEval=swarm(i).gbest.eval;
     %%% now update the global best...      
     if (isBest( eval,globalBestEval))
         %%% update global in all swarms... 
         %%% update globals... 
         i
         for j= 1 : swarm_size
              swarm(j).gbest=swarm(i).current;
              swarm(j).gbest.eval=eval; 
         end 
         
           globalBest =swarm(i).current;
            globalBestEval=eval ; 
     end 
     
     
     %%now update the particle 
      %%% first update local best 
      if (isBest( eval,localBestEval))
         %%% update local best of the current particle. 
         swarm(i).lbest=swarm(i).current;
          swarm(i).lbest.eval=eval; 
         end 
  end  % for all swarm 
  
  %%-----------------------after all update we must move all particles... 
  
  
  for i = 1 : swarm_size
        p=swarm(i).current;
           %r1=randn(1,1);
           %r2=randn(1,1);
        [rs cs]=size(p.particle);
           r1=randn(1,1);
           r2=randn(1,1);
      for r=1:rs  % for each value in the particle 
          for c=1:cs
          
     % update the velocity 
     p.vel(r,c)= p.vel(r,c)*W + c1 *r1* (swarm(i).lbest.particle(r,c)-p.particle(r,c)) + c2 *r2* (swarm(i).gbest.particle(r,c)-p.particle(r,c));
       if (abs(p.vel(r,c))> vmax)
          p.vel(r,c)= p.vel(r,c)/(2.0*vmax) ;
       end 
%% then move the paricle.... 
    p.particle(r,c)= p.particle(r,c)+p.vel(r,c);
          end 
      end
        
      swarm(i).current=p;
      % swarm(i).current.particle
      
  end
  
  
  %%%%%% draw the swarm... ....
  
%  % figure
  newplot
   hold on 
% %for i = 1 : swarm_size
%  %          p=swarm(i).current;
   plot(swarm(1).current.particle(1,:),swarm(1).current.particle(2,:),'r.','MarkerSize',12);
   plot(swarm(2).current.particle(1,:),swarm(2).current.particle(2,:),'b.','MarkerSize',12);
   plot(swarm(3).current.particle(1,:),swarm(3).current.particle(2,:),'c.','MarkerSize',12);
   plot(swarm(4).current.particle(1,:),swarm(4).current.particle(2,:),'m.','MarkerSize',12);
   plot(swarm(5).current.particle(1,:),swarm(5).current.particle(2,:),'g.','MarkerSize',12);
     drawnow
     hold off
%end
  
  
  [globalBestEval,finalC]=evalParticle(globalBest,data);
  
  idx=finalC;
 newplot 
  hold on 
plot(data(idx==1,1),data(idx==1,2),'r.','MarkerSize',12)
plot(data(idx==2,1),data(idx==2,2),'b.','MarkerSize',12) 
plot(data(idx==3,1),data(idx==3,2),'g.','MarkerSize',12) 
plot(data(idx==4,1),data(idx==4,2),'c.','MarkerSize',12) 
plot(data(idx==5,1),data(idx==5,2),'m.','MarkerSize',12)


plot(data(idx==6,1),data(idx==6,2),'y.','MarkerSize',12) 
 plot(data(idx==7,1),data(idx==7,2),'k.','MarkerSize',12) 
 plot(data(idx==8,1),data(idx==8,2),'+','MarkerSize',10) 
drawnow 
   end 
   
[globalBestEval,finalC]=evalParticle(globalBest,data);
    
    
   save  GlobalBest  globalBest  finalC
   
   
   %%% after all iteration there is the result is in gbest... 