swarm_size=30; 
iterations = 20;
c1=2;
c2=2;
 vmax=6;
W = 3.0;
correction_factor = 2.0;
N=10; % size of each particle.... 
 wi=80;
 hi=60;
wh=wi*hi;

 for i = 1 : swarm_size
     
  swarm(i).gbest = struct('particle',randn(wh,N) , 'vel', randn(N,1) , 'eval', 0.0);
  %swarm(i).gbest = struct('particle',randn(N,1) , 'vel', randn(N,1) , 'eval', 0.0);
  swarm(i).gbest.eval=evalParticle( swarm(i).gbest);
  
  %swarm(i).lbest = struct('particle',randn(N,1) , 'vel', randn(N,1) , 'eval', 0.0);
  swarm(i).lbest = struct('particle',randn(wh,N) , 'vel', randn(wh,N) , 'eval', 0.0);
  swarm(i).lbest.eval=evalParticle( swarm(i).lbest);
    
    %swarm(i).current = struct('particle',randn(N,1) , 'vel', randn(N,1) , 'eval', 0.0); 
    swarm(i).current = struct('particle',randn(wh,N) , 'vel', randn(wh,N) , 'eval', 0.0); 
    swarm(i).current.eval=evalParticle( swarm(i).current);
 end
   globalBest = struct('particle',randn(N,1) , 'vel', randn(N,1) , 'eval', 0.0);
   globalBest = struct('particle',randn(wh,N) , 'vel', randn(wh,N) , 'eval', 0.0);

 globalBestEval=evalParticle( globalBest);
 
   for iter = 1 : iterations    % major swarm iteration... 
   iter 
  for i = 1 : swarm_size
      
       %-- FIRST evaluate position
     eval=evalParticle( swarm(i).current);
     localBestEval=swarm(i).lbest.eval;
     
      %%now update the particle 
      %%% first update local best 
         if (isBest( eval,localBestEval))
            i
         %%% update local best of the current particle. 
         swarm(i).lbest=swarm(i).current;
         end 
    % globalBestEval=swarm(i).gbest.eval;
     %%% now update the global best...      
     if (isBest( eval,globalBestEval))
         %%% update global in all swarms... 
         %%% update globals... 
         i
         for j= 1 : swarm_size
              swarm(j).gbest=swarm(i).current;
         end 
         
           globalBest =swarm(i).current;
            globalBestEval=swarm(i).current.eval
     end 
     
  end  % for all swarm 
  
  %%-----------------------after all update we must move all particles... 
  
  
  for i = 1 : swarm_size
        p=swarm(i).current;
           r1=randn(1,1);
        r2=randn(1,1);
        [rs cs]=size(p.particle);
        
      for r=1:rs  % for each value in the particle 
          for c=1:cs
     % update the velocity 
     p.vel(r,c)= p.vel(r,c)*W + c1 *r1* (swarm(i).lbest.particle(r,c)-p.particle(r,c)) + c2 *r2* (swarm(i).gbest.particle(r,c)-p.particle(r,c));
%      if (p.vel(j)> vmax)
%          
%      end 
%% then move the paricle.... 
    p.particle(r,c)= p.particle(r,c)+p.vel(r,c);
          end 
      end
      swarm(i).current=p;
      
  end
   end 
   
   
   %%% after all iteration there is the result is in gbest... 