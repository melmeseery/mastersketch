clear all 
clc 

swarm_size=30; 
iterations = 20;
 wi=80;
 hi=60;
c1=1.5;
c2=1.5;
 vmax=20;
W = 2.5;
correction_factor = 2.0;
ConstantValue=100;
N=1; % size of each particle.... 
wh=50;

%data=10*randn(2500,2);
 load Data
 data=DataVector;
 
 
 
 vec=getWeights(data,43);
N=50;%% number of clusters.... in particle 
wh = size(data,2);  %% data in each cluster 
   %globalBest = struct('particle',randn(N,1) , 'vel', randn(N,1) , 'eval', 0.0);
 globalBest = struct('particle',getRandomW(wh,N,vec), 'vel', randn(wh,N) , 'eval', 0.0,'FincalClusters',zeros(wh,N),'elements',zeros(wh,N));
 [globalBestEval,finalC]=evalParticle(globalBest,data);

 for i = 1 : swarm_size
   %   vec=getWeights(data,i*2);
%ConstantValue=randn(1,1)*100     
  swarm(i).gbest = globalBest; 
 swarm(i).gbest.eval =globalBestEval;
  

  swarm(i).lbest = struct('particle',getRandomW(wh,N,vec) , 'vel', randn(wh,N) , 'eval', 0.0,'FincalClusters',zeros(wh,N),'elements',zeros(wh,N));
  [swarm(i).lbest.eval,ffff]=evalParticle( swarm(i).lbest,data);
    
    swarm(i).current = struct('particle',getRandomW(wh,N,vec) , 'vel', randn(wh,N) , 'eval', 0.0,'FincalClusters',zeros(wh,N),'elements',zeros(wh,N)); 
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
           r1=randi(1,1);
           r2=randi(1,1);
     % update the velocity 
        swarm(i).current.vel(r,c)= p.vel(r,c)*W + c1 *r1* (swarm(i).lbest.particle(r,c)-p.particle(r,c)) + c2 *r2* (swarm(i).gbest.particle(r,c)-p.particle(r,c));
       if (abs(swarm(i).current.vel(r,c))> vmax)
          swarm(i).current.vel(r,c)= swarm(i).current.vel(r,c)/(2.0*vmax) ;
       end 
%% then move the paricle.... 
      swarm(i).current.particle(r,c)=   swarm(i).current.particle(r,c)+   swarm(i).current.vel(r,c);
          end 
      end
        
     % swarm(i).current=p;
      % swarm(i).current.particle
       swarm(i).current.particle=RefineParticle(swarm(i).current.particle,data);
  end
  
  
  %%%%%% draw the swarm... ....
  
%  % figure
%  newplot
%    hold on 
% % %for i = 1 : swarm_size
% %  %          p=swarm(i).current;
%    plot(swarm(1).current.particle(1,:),swarm(1).current.particle(2,:),'r.','MarkerSize',12);
%    plot(swarm(2).current.particle(1,:),swarm(2).current.particle(2,:),'b.','MarkerSize',12);
%    plot(swarm(3).current.particle(1,:),swarm(3).current.particle(2,:),'c.','MarkerSize',12);
%    plot(swarm(4).current.particle(1,:),swarm(4).current.particle(2,:),'m.','MarkerSize',12);
%    plot(swarm(5).current.particle(1,:),swarm(5).current.particle(2,:),'g.','MarkerSize',12);
%      drawnow
%      hold off
% %end
  
  
%   [globalBestEval,finalC]=evalParticle(globalBest,data);
%   
[globalBestEval,finalC]=evalParticle(globalBest,data);
  idx=finalC;
ctrs=globalBest.particle;
 newplot 
  hold on 
plot(data(idx==1,1),data(idx==1,2),'r.','MarkerSize',12)
plot(data(idx==2,1),data(idx==2,2),'b.','MarkerSize',12) 
plot(data(idx==3,1),data(idx==3,2),'g.','MarkerSize',12) 
plot(data(idx==4,1),data(idx==4,2),'c.','MarkerSize',12) 
plot(data(idx==5,1),data(idx==5,2),'m.','MarkerSize',12)


plot(data(idx==6,1),data(idx==6,2),'y.','MarkerSize',12) 
 plot(data(idx==7,1),data(idx==7,2),'h','MarkerSize',12) 
 plot(data(idx==8,1),data(idx==8,2),'+','MarkerSize',12) 
plot(ctrs(1,1:8),ctrs(2,1:8),'kx', 'MarkerSize',12,'LineWidth',2)
plot(ctrs(1,1:8),ctrs(2,1:8),'ko', 'MarkerSize',12,'LineWidth',2)

 drawnow 
   end 
   
[globalBestEval,finalC]=evalParticle(globalBest,data);
    
    
   save  GlobalBest  globalBest  finalC
  % save  GlobalBestRandom  globalBest  finalC
   
   %%% after all iteration there is the result is in gbest... 