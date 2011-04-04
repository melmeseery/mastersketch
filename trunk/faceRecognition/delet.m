function delet(database_directory,persons_number,person_samples)
del_dir = input('Input the directory     ','s');
del_number = input('Input number of persons you want to delete     ');
for i=1:del_number
    for j=0:person_samples
        copyfile(strcat(del_dir,num2str(i),'.',num2str(j),'.jpg'),strcat(database_directory,num2str(i+persons_number),'.',num2str(j),'.jpg'));
    end
end
addpreparing(database_directory,persons_number,added_number,person_samples)
