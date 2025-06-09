import {Button, Empty, Form, Input, Modal, Table} from "antd";
import {FC, useState} from "react";
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
    const [showCreateNewModal, setShowCreateNewModal] = useState<boolean>(false);
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
            <Button data-testid="add-books" onClick={() => setShowCreateNewModal(true)}>Add Book</Button>
            <Modal
                title={'Add new Book'}
                open={showCreateNewModal}
                onCancel={() => setShowCreateNewModal(false)}
                footer={'null'}
            >
                <Form autoComplete="off">
                    <Form.Item label={'Title'} htmlFor="title">
                        <Input id="title" aria-label="title"/>
                    </Form.Item>
                    <Form.Item label={'Author'} htmlFor="author">
                        <Input id="author" aria-label="author"/>
                    </Form.Item>
                    <Form.Item label={'Description'} htmlFor="description">
                        <Input.TextArea id="description" aria-label="description" rows={4}/>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};
