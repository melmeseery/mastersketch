function par=RefineParticle(particle,data)
%  this is the particle 
par=particle; 
% 
% 
% type=1;
%  clusterData={};
% k=size(data,1); % number of data points in clusters... 
% [rows csize]=size(par);
% total=zeros(1,csize); 
% elements=zeros(1,csize);
% finalC=zeros(1,k);
% dis=[];
% for j=1:k  %% fore each data sample... in database... 
%  %%% for each cluster do the following... 
%  for i=1:csize
%  %Squared Euclidean distance  between centers and points
%  %dis(i)=distance_measure( p.particle(i) , face_fets(:,j),type);
%  dis(i)=distance_measure(par(:,i)' , data(j,:),type);
%  end
%   %% assign to cluster 
% [value c]=min(dis) ;
%  
% 
% % value is the min distance... and c is the cluster number. 
% 
% 
%  finalC(j)=c;
%  
%  total(c)=total(c)+value;
%  
%  elements(c)= elements(c)+1;
%  clusterData{c}=[ clusterData{c}; data(j,:) ]
%  
%  
% end 
% 
% 
% %%%% now edit the oarticle based on the cluster .
% 
%  for c=1:csize
%  
%  par(:,c) = mean( clusterData{c}');
%  end
% 
% end 

%%%% in FinalC(j) ==gives the cluster for each 



