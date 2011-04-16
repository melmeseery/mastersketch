function secondpreparing(persons_number,person_samples, database_directory)
%obtain the mean image 
wi=80;
hi=60;

face=zeros(wi,hi,3);
figure ; 
%calculate the average face in face space
for i=1:persons_number
    for j=0:person_samples-1
        facetemp=imread(strcat( database_directory,num2str(i),'.',num2str(j),'.jpg'));
        
%%me=mean(temp);
% st=std(temp);
% temp=(temp-me)*ustd/st+um;
% NormImage = temp;
        
       % imshow(facetemp);
        %size(facetemp)
      face=face+double(facetemp); 
    end
 %   i
end
face=face/(persons_number*person_samples);

face=uint8(face);
face=rgb2gray(face);

imwrite(face,'./average database/face.jpg');
face=double(face);
C=zeros();
disp(' now getting the.......... ')

for i=1:persons_number
  %  i
    person_face_sum=zeros(wi,hi);
    for j=0:person_samples-1
        person_face=imread(strcat( database_directory,num2str(i),'.',num2str(j),'.jpg'));
        person_face=rgb2gray(person_face);
        person_face=double(person_face);
        person_face_sum=person_face_sum+person_face;
    end
person_face_avg = person_face_sum/person_samples;

imwrite(uint8(person_face_avg),strcat('./average database/',num2str(i),'.jpg'));
%obtain the difference between each person and the mean
person_face_diff_avg = person_face_avg - face ;
person_face_total_avg(:,i) = reshape(person_face_diff_avg,wi*hi,1);
end

%%%  this is C=A' * A
 C = person_face_total_avg' * person_face_total_avg;
[vv dd] = eig(C)  %% produces matrices of eigenvalues (D) and eigenvectors (V) of matrix C
% Sort and eliminate those whose eigenvalue is zero
v=[];
d=[];
for i=1:size(vv,2)
if(dd(i,i)>1e-4)
v=[v vv(:,i)];
d=[d dd(i,i)];
end
end

%sort, will return an ascending sequence
[B index]=sort(d);
ind=zeros(size(index));
dtemp=zeros(size(index));
vtemp=zeros(size(v));
len=length(index);
for i=1:len
dtemp(i)=B(len+1-i);
ind(i)=len+1-index(i);
vtemp(:,ind(i))=v(:,i);
end
d=dtemp;%% eidgen values 
v=vtemp;  %% eigen vecctors.... 

  
% %normalization of eigen vectors
u=[];  %% get the u, eigen facess. 
for i=1:size(v,2)
    temp = sqrt(d(i));
   u=[u person_face_total_avg * v(:,i)/temp];
end

%normalization of eigen vectors
%for i=1:size(u,2)
 %   kk=u(:,i);
  %  temp = sqrt(sum(kk.^2));
   % u(:,i)=u(:,i)./temp;
%end
%break;
eigen_faces=u(:,1:persons_number);  % this is the same matrix.
%face_fets=eigen_faces'*person_face_total_avg;
face_fets=[];
for k=1:size(person_face_total_avg,2)  %% for each person in db. .. 
    WW=[]; %% empty weigh.. 
    for i=1:size(u,2) %% for each u vector. 
        %%%% get weight of each person.... 
        Weightofimage=eigen_faces(:,i)' * person_face_total_avg(:,k);
        WW=[WW ; Weightofimage];
    end
    face_fets = [face_fets WW];
end

save('./average database/fets1', 'eigen_faces','face_fets','-append');

for i=1:size(u,2) %% for each 
    img=reshape(u(:,i),wi,hi);
   %  img=img';
   img = histeq(img,255);
 %   subplot(10,5,i)
    imshow(img)
    drawnow ;
    imwrite((img),strcat('./eigen faces1/',num2str(i),'.jpg'));
end 
%*****************************************************************
% face_cov=cov(person_face_total_avg); 
% [eigen_vectors eigen_values]=eig(face_cov);
% diag_eigen_values=diag(eigen_values);
% [esort_result esort_index]=sort(diag_eigen_values);
% 
% for i=1:persons_number
%         esort_eigen_vectors(:,i)=eigen_vectors(:,esort_index(i));
% end
% eigen_faces=person_face_total_avg*esort_eigen_vectors(:,1:persons_number);
% 
% for i=1:size(eigen_faces,2)
%     img=reshape(eigen_faces(:,i),80,60);
%    %  img=img';
%    img = histeq(img,255);
%     subplot(10,5,i)
%     imshow(img)
%     drawnow ;
%     imwrite((img),strcat('./eigen faces2/',num2str(i),'.jpg'));
% end 
% 
% face_fets=eigen_faces'*person_face_total_avg;
% save('./average database/fets', 'eigen_faces','face_fets','-append');
