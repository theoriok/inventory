import {Table} from "antd";
import {FC} from "react";
import {useBooks} from "../hooks/book.hook.ts";

export const HomePage: FC = () => {
    const {data: books} = useBooks();
    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
        },
        {
            title: 'Author',
            dataIndex: 'author',
            key: 'author',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
    ];
    console.log(books);
    const data = [
        {"title": "Lord of the Rings", "author": "J.R.R. Tolkien", "description": "Best book of the 20th century."},
        {"title": "Game of Thrones", "author": "George R.R. Martin", "description": "Best book of the 21st century."},
    ];
    return (
        <>
            <h1>Books</h1>
            <Table dataSource={data} columns={columns} showHeader={true}></Table>
        </>
    );
};
