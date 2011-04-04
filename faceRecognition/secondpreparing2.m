function secondpreparing(persons_number,person_samples)
%obtain the mean image 
face=zeros(80,60,3);
for i=1:persons_number
    for j=0:person_samples-1
      face=face+double(imread(strcat('d:\heba\facerecog\new\color dbase1\',num2str(i),'.',num2str(j),'.jpg'))); 
    end
end
face=face/(persons_number*person_samples);

face=uint8(face);
face=rgb2gray(face);

imwrite(face,'d:/heba/facerecog/new/average database/face.jpg');
face=double(face);
for i=1:persons_number
    person_face_sum=zeros(80,60);
    for j=0:person_samples-1
        person_face=imread(strcat('d:/heba/facerecog/new/color dbase1/',num2str(i),'.',num2str(j),'.jpg'));
        person_face=rgb2gray(person_face);
        person_face=double(person_face);
        person_face_sum=person_face_sum+person_face;
    end
person_face_avg = person_face_sum/person_samples;

imwrite(uint8(person_face_avg),strcat('d:/heba/facerecog/new/average database/',num2str(i),'.jpg'));
%obtain the difference between each person and the mean
person_face_diff_avg = face - person_face_avg ;
person_face_total_avg(:,i) = reshape(person_face_diff_avg,80*60,1);
end
 %   person_face_avg = person_face_sum/person_samples;
  %  imwrite(uint8(person_face_avg),strcat('c:/heba/new/average database/',num2str(i),'.jpg'));
%person_face_diff_avg = person_face_avg - face;
 % person_face_total_avg(:,i) = reshape(person_face_diff_avg,80*60,1);
C = (person_face_total_avg'* person_face_total_avg)/persons_number;
[vv dd] = eig(C)
diag_eig_values=diag(dd);
[esort_res esort_indx]=esort(diag_eig_values);
for i=1:persons_number
        esort_eig_vectors(:,i)=vv(:,esort_indx(i));
end
%eigen_faces=person_face_total_avg*esort_eig_vectors(:,1:10);
%face_fets=eigen_faces'*person_face_total_avg;
%save('c:/heba/new/average database/fets1', 'eigen_faces','face_fets','-append');
% Show eigenfaces
u=[];
for i=1:size(esort_eig_vectors,2)
    temp = sqrt(esort_res(i));
   u(:,i)=person_face_total_avg * esort_eig_vectors(:,i)/temp;
end

%normalization of eigen vectors
%for i=1:size(u,2)
 %   kk=u(:,i);
  %  temp = sqrt(sum(kk.^2));
   % u(:,i)=u(:,i)./temp;
%end
eigen_faces=u(:,1:persons_number);
%face_fets=eigen_faces'*person_face_total_avg;
face_fets=[];
for k=1:size(person_face_total_avg,2)
    WW=[];
    for i=1:size(u,2)
        Weightofimage=eigen_faces(:,i)' * person_face_total_avg(:,k);
        WW=[WW ; Weightofimage];
    end
    face_fets = [face_fets WW];
end

save('d:/heba/facerecog/new/average database/fets1', 'eigen_faces','face_fets','-append');

for i=1:size(u,2)
    img=reshape(u(:,i),80,60);
   %  img=img';
   img = histeq(img,255);
 %   subplot(10,5,i)
    imshow(img)
    drawnow ;
    imwrite((img),strcat('d:/heba/facerecog/new/eigen faces1/',num2str(i),'.jpg'));
end 
%*****************************************************************
face_cov=cov(person_face_total_avg); 
[eigen_vectors eigen_values]=eig(face_cov);
diag_eigen_values=diag(eigen_values);
[esort_result esort_index]=esort(diag_eigen_values);

for i=1:persons_number
        esort_eigen_vectors(:,i)=eigen_vectors(:,esort_index(i));
end
eigen_faces=person_face_total_avg*esort_eigen_vectors(:,1:persons_number);
for i=1:size(eigen_faces,2)
    img=reshape(eigen_faces(:,i),80,60);
   %  img=img';
   img = histeq(img,255);
    subplot(10,5,i)
    imshow(img)
    drawnow ;
    imwrite((img),strcat('d:/heba/facerecog/new/eigen faces2/',num2str(i),'.jpg'));
end 

face_fets=eigen_faces'*person_face_total_avg;
save('d:/heba/facerecog/new/average database/fets', 'eigen_faces','face_fets','-append');
