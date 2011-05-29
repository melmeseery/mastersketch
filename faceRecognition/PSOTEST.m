%%% testing PSO.... 
 clear all 
% 
 load Data 
 load GlobalBest
 
 cin = globalBest.particle;
 
  data=DataVector;
% 
  %[indx,ctrs]=kmeans(data,50);
  
  
 % [globalBestEval,finalC]=evalParticle(globalBest,data);
 % idx=finalC;
%ctrs=globalBest.particle;
% idx=indx; 
%  newplot 
%   hold on 
% plot(data(idx==1,1),data(idx==1,2),'r.','MarkerSize',12)
% plot(data(idx==2,1),data(idx==2,2),'b.','MarkerSize',12) 
% plot(data(idx==3,1),data(idx==3,2),'g.','MarkerSize',12) 
% plot(data(idx==4,1),data(idx==4,2),'c.','MarkerSize',12) 
% plot(data(idx==5,1),data(idx==5,2),'m.','MarkerSize',12)
% 
% 
% plot(data(idx==6,1),data(idx==6,2),'y.','MarkerSize',12) 
%  plot(data(idx==7,1),data(idx==7,2),'h','MarkerSize',12) 
%  plot(data(idx==8,1),data(idx==8,2),'+','MarkerSize',12) 
% plot(ctrs(1,1:8),ctrs(2,1:8),'kx', 'MarkerSize',12,'LineWidth',2)
% plot(ctrs(1,1:8),ctrs(2,1:8),'ko', 'MarkerSize',12,'LineWidth',2)
% 
%  drawnow 
%  
%  
%  save DataMeans indx ctrs 
%  
  load DataMeans
 
 %    load  GlobalBest  globalBest  finalC
%    
%    
%    data=randn(25000,2);
%    
 %%% 
 
 
 
 
 
% idx=indx;
% 
%  idx=indx;
%   hold on 
% plot(data(idx==1,1),data(idx==1,2),'r.','MarkerSize',12)
%  plot(data(idx==2,1),data(idx==2,2),'b.','MarkerSize',12) 
%  plot(data(idx==3,1),data(idx==3,2),'g.','MarkerSize',12) 
%  plot(data(idx==4,1),data(idx==4,2),'c.','MarkerSize',12) 
%  plot(data(idx==5,1),data(idx==5,2),'m.','MarkerSize',12) 
%  
%  plot(data(idx==6,1),data(idx==6,2),'y.','MarkerSize',12) 
%  plot(data(idx==7,1),data(idx==7,2),'k.','MarkerSize',12) 
%  plot(data(idx==8,1),data(idx==8,2),'+','MarkerSize',10) 
 
 
 
 face= double(imread('./average database/face.jpg'));
load('./average database/fets1');


%     im=imread(strcat('./color dbase1/',num2str(6),'.',num2str(2),'.jpg'));
% 
%  im_face=reshape(double(rgb2gray(double(im))), 80*60,1);
%   InIm=[];
%       for j=1:size(eigen_faces,2)
%      test_face_fets=dot(eigen_faces(:,j)',im_face');
%        InIm = [InIm;test_face_fets];
%  end;
%  wi=InIm;    
% [test_face_fets,wi]=getFaceEigen(im,face,eigen_faces,1);
%wi=ctrs(:,1);
 % wi=ctrs(:,5);
 i = 50;
 for i=1:150
  
wi=DataVector(i,:)';
wi2=cin(:,finalC(i));
%wi2=cin(finalC(i),:)';
%wi2=cin(13,:)';
wi3=ctrs(indx(i),:)';


      %wi1=wi(6);
     m=reshape(face,80*60,1);
     rs=eigen_faces(:,1:50)*wi;
  ReshapedImage = rs +m ;

  
     rs2=eigen_faces(:,1:50)*wi2;
  ReshapedImage2 = rs2 +m ;
  
       rs3=eigen_faces(:,1:50)*wi3;
  ReshapedImage3 = rs3 +m ;
  
  
   img=reshape(ReshapedImage2,80,60);
 im2=reshape(ReshapedImage,80,60); 
 im3=reshape(ReshapedImage3,80,60);
 %img=histeq(im2);
 subplot(3,1,2);
  imshow(img,[0 255]);
  title(' PSO Cluster centroid reconstruction ');
 subplot(3,1,1);
 imshow(im2,[0  255]);  
 title(' Orginal Image Reconstruction ')
 subplot(3,1,3);
 imshow(im3,[0  255]); 
  title(' Kmeans PSO Cluster centroid reconstruction ');
 drawnow
    pause
 end 
%   subplot(4,1,4)
%  imshow( face,[0  255]);
 
 
% for di=1:2:20
%  
%     figure
%     hold on 
% plot(data(idx==2,di),data(idx==2,di+1),'b.','MarkerSize',12)
% 
% plot(data(idx==3,di),data(idx==3,di+1),'g.','MarkerSize',12)
% plot(data(idx==4,di),data(idx==4,di+1),'c.','MarkerSize',12)
% plot(data(idx==5,di),data(idx==5,di+1),'m.','MarkerSize',12)
% plot(data(idx==6,di),data(idx==6,di+1),'y.','MarkerSize',12)
% 
% end 

% plot(ctrs(:,1),ctrs(:,2),'MarkerSize',12,'LineWidth',2)
% plot(ctrs(:,1),ctrs(:,2),'MarkerSize',12,'LineWidth',2)
%  
% legend('Cluster 1','Cluster 2','Centroids',  'Location','NW')