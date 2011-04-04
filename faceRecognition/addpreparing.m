function addpreparing(database_directory,persons_number,added_number,person_samples)
face=double(imread('./average database/face.jpg'));
face = face * persons_number * person_samples;
added_face = zeros(240,320,3);
for i=persons_number + 1:persons_number + added_number
    for j=1:person_samples
        added_face = added_face + double(imread(strcat(database_directory,num2str(i),'.',num2str(j),'.jpg'))); 
    end
end
added_face=imresize(added_face,[80 60]);
added_face=uint8(added_face);
added_face=rgb2gray(added_face);
added_face=double(added_face);
face = (face + added_face)/((persons_number+added_number)*person_samples);
imwrite(uint8(face),'d:/new/average database/face.jpg');
for i=1:persons_number
    person_face_avg = double(imread(strcat('./average database/',num2str(i),'.jpg')));
    person_face_diff_avg = face - person_face_avg;
    person_face_total_avg(:,i) = reshape(person_face_diff_avg,80*60,1);
end
for i=persons_number + 1:persons_number + added_number
    person_face_sum = zeros(80,60);
    for j=1:person_samples 
        person_face=imread(strcat(database_directory,num2str(i),'.',num2str(j),'.jpg'));
        person_face=rgb2gray(person_face);
        person_face=imresize(person_face,[80 60]);
        person_face=double(person_face);
        person_face_sum=person_face_sum+person_face;
    end
    person_face_avg = person_face_sum/person_samples;
    imwrite(uint8(person_face_avg),strcat('./average database/',num2str(i),'.jpg'));
    person_face_diff_avg = face - person_face_avg;
    person_face_total_avg(:,i) = reshape(person_face_diff_avg,80*60,1);
end
face_cov=cov(person_face_total_avg); 
[eigen_vectors eigen_values]=eig(face_cov);
diag_eigen_values=diag(eigen_values);
[esort_result esort_index]=esort(diag_eigen_values);
persons_number = persons_number + added_number;
for i=1:persons_number
        esort_eigen_vectors(:,i)=eigen_vectors(:,esort_index(i));
end
eigen_faces=person_face_total_avg*esort_eigen_vectors(:,1:20);
face_fets=eigen_faces'*person_face_total_avg;
save('./average database/fets','persons_number','eigen_faces','face_fets','-append');