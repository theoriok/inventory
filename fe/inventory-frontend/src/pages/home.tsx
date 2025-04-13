import {Empty, Table} from "antd";
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
    return (
        <>
            <h1>Books</h1>
            <Table dataSource={books?.items} columns={columns} showHeader={true} locale={{
                emptyText: (
                    <Empty
                        image={Empty.PRESENTED_IMAGE_SIMPLE}
                        description="No Books Available"
                        data-testid="no-books-message"
                    />
                ),
            }} data-testid="books-table"></Table>
        </>
    );
};
